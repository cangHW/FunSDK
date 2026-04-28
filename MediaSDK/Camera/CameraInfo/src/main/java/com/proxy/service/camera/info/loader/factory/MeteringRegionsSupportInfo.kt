package com.proxy.service.camera.info.loader.factory

import android.graphics.Rect
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/4/29 14:15
 * @desc:
 */
class MeteringRegionsSupportInfo {

    companion object {
        private const val TAG = "${CameraConstants.TAG}MeteringRegionsSupportInfo"
    }

    private var meteringAreaSupports = CsExcellentMap<String, RegionsSupport>()


    fun getRegionsSupport(manager: CameraManager, cameraId: String): RegionsSupport {
        var support = meteringAreaSupports.get(cameraId)
        if (support == null) {
            try {
                support = createRegionsSupport(manager, cameraId)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
            if (support == null) {
                support = RegionsSupport()
            }
            meteringAreaSupports.putSync(cameraId, support)
        }
        return support
    }

    private fun createRegionsSupport(manager: CameraManager, cameraId: String): RegionsSupport {
        val info = RegionsSupport()

        val characteristics = manager.getCameraCharacteristics(cameraId)

        try {
            val maxRegions: Int? = characteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)
            info.isSupportRegionsAf = maxRegions != null && maxRegions > 0
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }

        try {
            val maxRegions: Int? = characteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE)
            info.isSupportRegionsAe = maxRegions != null && maxRegions > 0
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }

        if (!info.isCanUse()) {
            return info
        }

        try {
            info.activeArraySizeRect =
                characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }

        return info
    }


    class RegionsSupport {

        /**
         * 是否支持 AF 区域
         * */
        var isSupportRegionsAf: Boolean = false

        /**
         * 是否支持 AE 区域
         * */
        var isSupportRegionsAe: Boolean = false

        /**
         * 实际产生有效图像数据的像素区域
         * */
        var activeArraySizeRect: Rect? = null

        fun isCanUse(): Boolean {
            return isSupportRegionsAf || isSupportRegionsAe
        }
    }
}