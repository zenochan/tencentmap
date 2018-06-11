package zeno.name.ext.tencentmap

import android.content.Context
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/11
 */
internal class LocationImpl(context: Context) : ILocation, TencentLocationListener {
  private val locationManager = TencentLocationManager.getInstance(context)
  private var subject: PublishSubject<TencentLocation>? = null

  private val observables = ArrayList<Observable<TencentLocation>>()

  /**
   * @param name  GPS，Wi-Fi等
   * @param status  新的状态, 启用或禁用
   * @param desc  状态描述
   */
  override fun onStatusUpdate(name: String, status: Int, desc: String) {}

  /**
   * @param location  新的位置
   * @param error  错误码
   * @param reason  错误描述
   */
  override fun onLocationChanged(location: TencentLocation, error: Int, reason: String) {
    if (error == TencentLocation.ERROR_OK) {
      subject?.onNext(location)
    } else {
      subject?.onError(IllegalStateException(reason))
    }
  }

  override fun requestLocation(take: Long, type: Int): Observable<TencentLocation> {
    var sub: Observable<TencentLocation> = subject ?: let {
      val sub = PublishSubject.create<TencentLocation>()
      val request = TencentLocationRequest.create()
      request.requestLevel = TencentLocationRequest.REQUEST_LEVEL_POI
      request.interval = 1000


      // 0	注册位置监听器成功
      // 1	设备缺少使用腾讯定位SDK需要的基本条件
      // 2	配置的 Key 不正确
      // 3	自动加载libtencentloc.so失败，可能由以下原因造成：
      //    1、这往往是由工程中的so与设备不兼容造成的，应该添加相应版本so文件;
      //    2、如果您使用AndroidStudio,可能是gradle没有正确指向so文件加载位置，可以按照这里配置您的gradle;
      when (locationManager.requestLocationUpdates(request, this)) {
        0 -> Unit
        1 -> sub.onError(IllegalStateException("设备缺少使用腾讯定位SDK需要的基本条件"))
        2 -> sub.onError(IllegalStateException("配置的 Key 不正确"))
        3 -> sub.onError(IllegalStateException("自动加载libtencentloc.so失败"))
        else -> sub.onError(IllegalStateException("其他错误"))
      }

      subject = sub
      sub
    }

    sub = sub.doOnSubscribe {
      observables.add(sub)
    }.doOnDispose {
      onDown(sub)
    }.doOnError {
      onDown(sub)
    }.doOnComplete {
      onDown(sub)
    }
    if (take > 0) sub = sub.take(take)
    return sub
  }

  private fun onDown(item: Observable<TencentLocation>) {
    if (observables.contains(item)) {
      observables.remove(item)
    }
    if (observables.isEmpty()) {
      locationManager.removeUpdates(this)
      subject?.onComplete()
      subject = null
    }
  }
}