package zeno.name.tencentmap.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import name.zeno.ktrxpermission.ZPermission
import name.zeno.ktrxpermission.rxPermissions
import zeno.name.ext.tencentmap.ILocation

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
