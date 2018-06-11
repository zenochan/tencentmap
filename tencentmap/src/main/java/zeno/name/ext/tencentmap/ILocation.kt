package zeno.name.ext.tencentmap

import android.Manifest
import android.content.Context
import android.support.annotation.RequiresPermission
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationRequest
import io.reactivex.Observable

/**
 * @author [陈治谋](mailto:zenochan@qq.com)
 * @since 2018/6/11
 */
interface ILocation {


  /**
   *
   * @param take 接收结果次数
   * @param type 获取信息类型
   * - [TencentLocationRequest.REQUEST_LEVEL_GEO] 包含经纬度
   * - [TencentLocationRequest.REQUEST_LEVEL_NAME] 包含经纬度, 位置名称, 位置地址
   * - [TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA] 包含经纬度，位置所处的中国大陆行政区划
   * - [TencentLocationRequest.REQUEST_LEVEL_POI] 包含经纬度，位置所处的中国大陆行政区划及周边POI列表
   */
  @RequiresPermission(allOf = [
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
  ])
  fun requestLocation(take: Long = 1, type: Int = TencentLocationRequest.REQUEST_LEVEL_POI): Observable<TencentLocation>

  companion object {
    private var _instance: ILocation? = null
    @Synchronized
    fun instance(context: Context): ILocation {
      return _instance ?: let {
        val newInstance = LocationImpl(context)
        _instance = newInstance
        newInstance
      }
    }
  }

}