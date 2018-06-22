



### INSTALL

```groovy
android {
  defaultConfig{
    manifestPlaceholders = [TENCENT_AK: "AK"]
  } 
}
repositories{
  maven { url "http://maven.mjtown.cn/"}
}
dependencies {
  implementation "name.zeno.ext:tencentmap:0.0.1806111"
}
```



### [Android定位SDK](http://lbs.qq.com/geo/index.html)


```kotlin
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btn_location.setOnClickListener {
      rxPermissions(
          ZPermission.ACCESS_COARSE_LOCATION,
          ZPermission.ACCESS_FINE_LOCATION,
          ZPermission.READ_PHONE_STATE
      ).flatMap {
        if (it) ILocation.instance(this).requestLocation()
        else Observable.error(IllegalStateException("没有权限"))
      }.subscribe({
        tv_content.text = "${tv_content.text}\nnext: ${it.latitude},${it.longitude}"
      }, {
        tv_content.text = "${tv_content.text}\nerror: ${it.message}"
      }, {
        tv_content.text = "${tv_content.text}\ncomplete"
      })
    }
  }
}
```

- proguard
```proguard
-keepclassmembers class ** {
    public void on*Event(...);
}
-keep class c.t.**{*;}
-keep class com.tencent.map.geolocation.**{*;}
-keep class com.tencent.tencentmap.lbssdk.service.**{*;}
 
 
-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**
```