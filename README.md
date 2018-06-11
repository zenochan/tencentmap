




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