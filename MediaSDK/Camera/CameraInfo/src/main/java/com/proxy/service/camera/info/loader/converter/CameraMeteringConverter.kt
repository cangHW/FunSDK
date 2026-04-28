package com.proxy.service.camera.info.loader.converter

import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.MeteringRectangle
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.loader.CameraMeteringMode
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.info.loader.factory.CameraFactory
import com.proxy.service.camera.info.loader.factory.MeteringRegionsSupportInfo
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/23 18:26
 * @desc:
 */
object CameraMeteringConverter {

    private const val TAG = "${CameraConstants.TAG}MeteringConverter"

    /**
     * 将测光模式转换为 Camera2 请求参数。
     *
     * 1.自动模式会恢复连续 AF/自动 AE，并清空 AF、AE 区域；
     * 2.固定区域模式会把同一组区域按设备支持能力分别应用到 AF、AE。
     * */
    fun applyMeteringMode(
        cameraFaceMode: CameraFaceMode,
        cameraMeteringMode: CameraMeteringMode,
        cameraFunMode: CameraFunMode?,
        builder: CaptureRequest.Builder
    ) {
        if (cameraMeteringMode == CameraMeteringMode.AutoMode) {
            applyAutoMode(cameraFunMode, builder)
        } else if (cameraMeteringMode is CameraMeteringMode.FixedMode) {
            applyFixedMode(cameraFaceMode, cameraMeteringMode, cameraFunMode, builder)
        }
    }

    /**
     * 判断当前测光模式是否需要触发一次 AF。
     *
     * 只有固定区域模式、设备支持 AF 区域，并且区域有效时才需要发送
     * [CaptureRequest.CONTROL_AF_TRIGGER_START]。
     * */
    fun needsAfTrigger(faceMode: CameraFaceMode, meteringMode: CameraMeteringMode): Boolean {
        return meteringMode is CameraMeteringMode.FixedMode
                && getRegionsSupport(faceMode)?.isSupportRegionsAf == true
                && hasValidArea(meteringMode)
    }


    /**
     * 应用自动测光模式。
     *
     * 根据拍照/录像场景恢复连续 AF 模式，清空 AF、AE 区域，避免继续使用上一次触摸测光区域。
     * */
    private fun applyAutoMode(cameraFunMode: CameraFunMode?, builder: CaptureRequest.Builder) {
        val controlAfMode: Int = when (cameraFunMode) {
            CameraFunMode.RECORD -> {
                CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO
            }

            else -> {
                CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE
            }
        }
        builder.set(CaptureRequest.CONTROL_AF_MODE, controlAfMode)
        builder.set(CaptureRequest.CONTROL_AF_REGIONS, null)
        builder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON)
        builder.set(CaptureRequest.CONTROL_AE_REGIONS, null)
    }

    /**
     * 应用固定区域测光模式。
     *
     * 同一组区域会分别尝试下发到 AF、AE；如果设备不支持某一类区域，只跳过该类区域。
     * */
    private fun applyFixedMode(
        cameraFaceMode: CameraFaceMode,
        fixedMode: CameraMeteringMode.FixedMode,
        cameraFunMode: CameraFunMode?,
        builder: CaptureRequest.Builder
    ) {
        val regions = toMeteringRectangles(fixedMode)
        if (regions.isEmpty()) {
            applyAutoMode(cameraFunMode, builder)
            return
        }

        val support = getRegionsSupport(cameraFaceMode)
        if (support?.isSupportRegionsAf == true) {
            applyAfRegions(regions, builder)
        } else {
            CsLogger.tag(TAG).w("Metering AF region is not supported.")
            builder.set(CaptureRequest.CONTROL_AF_REGIONS, null)
        }

        if (support?.isSupportRegionsAe == true) {
            applyAeRegions(regions, builder)
        } else {
            CsLogger.tag(TAG).w("Metering AE region is not supported.")
            builder.set(CaptureRequest.CONTROL_AE_REGIONS, null)
        }
    }

    /**
     * 下发 AF 区域。
     *
     * AF 固定区域需要使用 [CameraMetadata.CONTROL_AF_MODE_AUTO]，
     * 真正的 AF 触发由 Controller 单独发送 AF trigger。
     * */
    private fun applyAfRegions(
        regions: Array<MeteringRectangle>,
        builder: CaptureRequest.Builder
    ) {
        builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO)
        builder.set(CaptureRequest.CONTROL_AF_REGIONS, regions)
    }

    /**
     * 下发 AE 区域。
     *
     * AE 区域不需要额外 trigger，设置到 repeating request 后由 Camera2 自动按区域测光。
     * */
    private fun applyAeRegions(
        regions: Array<MeteringRectangle>,
        builder: CaptureRequest.Builder
    ) {
        builder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON)
        builder.set(CaptureRequest.CONTROL_AE_REGIONS, regions)
    }

    /**
     * 将业务层测光区域转换为 Camera2 的 [MeteringRectangle]。
     *
     * 宽高无效的区域会被过滤，避免向 Camera2 下发非法区域。
     * */
    private fun toMeteringRectangles(
        fixedMode: CameraMeteringMode.FixedMode
    ): Array<MeteringRectangle> {
        val list = ArrayList<MeteringRectangle>()
        fixedMode.areas.forEach {
            if (it.width <= 0 || it.height <= 0) {
                return@forEach
            }

            list.add(
                MeteringRectangle(
                    it.x,
                    it.y,
                    it.width,
                    it.height,
                    it.weight
                )
            )
        }
        return list.toTypedArray()
    }

    /**
     * 判断固定区域模式中是否至少包含一个有效区域。
     * */
    private fun hasValidArea(fixedMode: CameraMeteringMode.FixedMode): Boolean {
        return fixedMode.areas.any { it.width > 0 && it.height > 0 }
    }

    /**
     * 获取当前摄像头对 AF、AE 区域测光的支持能力。
     * */
    private fun getRegionsSupport(
        cameraFaceMode: CameraFaceMode
    ): MeteringRegionsSupportInfo.RegionsSupport? {
        val cameraId = cameraFaceMode.getCameraId()
        if (cameraId.isNullOrEmpty()) {
            return null
        }
        return CameraFactory.getSensorRegionsSupport(cameraId)
    }

}