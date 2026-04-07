package com.proxy.service.camera.info.loader.converter

import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.MeteringRectangle
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraFunMode

/**
 * @author: cangHX
 * @data: 2026/3/23 18:26
 * @desc:
 */
object CameraAfConverter {

    /**
     * 处理对焦相关
     * */
    fun parse(
        cameraAfMode: CameraAfMode,
        cameraFunMode: CameraFunMode?,
        builder: CaptureRequest.Builder
    ) {
        if (cameraAfMode == CameraAfMode.AfAutoMode) {
            val afMode: Int? = when (cameraFunMode) {
                CameraFunMode.CAPTURE -> {
                    CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                }

                CameraFunMode.RECORD -> {
                    CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO
                }

                else -> {
                    null
                }
            }
            afMode?.let {
                builder.set(CaptureRequest.CONTROL_AF_MODE, afMode)
            }
        } else if (cameraAfMode is CameraAfMode.AfFixedMode) {
            val list = ArrayList<MeteringRectangle>()
            (cameraAfMode as? CameraAfMode.AfFixedMode?)?.list?.forEach {
                val area = MeteringRectangle(
                    it.x.coerceAtLeast(0),
                    it.y.coerceAtLeast(0),
                    it.width,
                    it.height,
                    it.weight
                )
                list.add(area)
            }

            if (list.isNotEmpty()) {
                builder.set(CaptureRequest.CONTROL_AF_REGIONS, list.toTypedArray())
                builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO)
                builder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START
                )
            }
        }
    }

}