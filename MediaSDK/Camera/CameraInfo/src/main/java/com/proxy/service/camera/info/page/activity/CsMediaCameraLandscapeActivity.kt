package com.proxy.service.camera.info.page.activity

import android.os.Bundle
import com.proxy.service.camera.info.page.manager.PictureRotationManager
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.SensorRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
class CsMediaCameraLandscapeActivity : CsMediaCameraActivity() {

    private var manager:PictureRotationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CsScreenUtils.addWeakSensorRotationCallback(sensorRotationCallback)

        manager = PictureRotationManager.create(
            params.pictureCaptureCallback,
            params.pictureCaptureByteCallback
        )

        params.pictureCaptureCallback = manager
        params.pictureCaptureByteCallback = manager
    }

    private val sensorRotationCallback = object : SensorRotationCallback {
        override fun onRotation(orientation: Int, rotation: RotationEnum) {
            val degree = when (rotation) {
                RotationEnum.ROTATION_0 -> {
                    90f
                }

                RotationEnum.ROTATION_270 -> {
                    180f
                }

                RotationEnum.ROTATION_180 -> {
                    -90f
                }

                else -> {
                    0f
                }
            }

            manager?.setRotation(degree)
        }
    }
}