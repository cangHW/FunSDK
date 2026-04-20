package com.proxy.service.camera.info.page.activity

import android.os.Bundle
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.info.page.manager.PictureRotationManager
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.SensorRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.permission.base.callback.ActionCallback

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
class CsMediaCameraPortraitActivity : CsMediaCameraActivity(), ActionCallback {

    companion object {

        private const val TAG = "${CameraConstants.TAG}PortraitActivity"
    }

    private var manager: PictureRotationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CsScreenUtils.addWeakSensorRotationCallback(sensorRotationCallback)

        val pParams = params.pictureCaptureParams
        manager = PictureRotationManager.create(
            pParams.pictureCaptureCallback,
            pParams.pictureCaptureByteCallback
        )

        pParams.pictureCaptureCallback = manager
        pParams.pictureCaptureByteCallback = manager
    }

    private val sensorRotationCallback = object : SensorRotationCallback {
        override fun onRotation(orientation: Int, rotation: RotationEnum) {
            val degree = when (rotation) {
                RotationEnum.ROTATION_90 -> {
                    -90f
                }

                RotationEnum.ROTATION_180 -> {
                    180f
                }

                RotationEnum.ROTATION_270 -> {
                    90f
                }

                else -> {
                    0f
                }
            }

            manager?.setRotation(degree)
        }
    }
}