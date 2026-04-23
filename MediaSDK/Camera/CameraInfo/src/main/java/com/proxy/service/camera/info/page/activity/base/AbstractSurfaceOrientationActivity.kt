package com.proxy.service.camera.info.page.activity.base

import android.os.Bundle
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.info.page.activity.CsMediaCameraLandscapeActivity.Companion.TAG
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.callback.SensorRotationCallback
import com.proxy.service.core.framework.system.screen.enums.RotationEnum

/**
 * @author: cangHX
 * @data: 2026/4/23 17:31
 * @desc:
 */
abstract class AbstractSurfaceOrientationActivity: AbstractUiChangedActivity() {

    private var sensorRotation: RotationEnum = RotationEnum.ROTATION_0
    private var screenRotation: RotationEnum = RotationEnum.ROTATION_0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CsScreenUtils.addWeakSensorRotationCallback(sensorRotationCallback)
        CsScreenUtils.addWeakScreenRotationCallback(screenRotationCallback, this)
        screenRotation = CsScreenUtils.getScreenRotation(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        CsScreenUtils.removeSensorRotationCallback(sensorRotationCallback)
        CsScreenUtils.removeScreenRotationCallback(screenRotationCallback)
    }

    override fun getSurfaceOrientation(): Int {
        val sign = if (cameraFaceMode == CameraFaceMode.FaceFront) -1 else 1
        return sign * (screenRotation.degree - sensorRotation.degree) * 90
    }

    private val screenRotationCallback = object : ScreenRotationCallback {
        override fun onRotation(rotation: RotationEnum) {
            CsLogger.tag(TAG).d("screenRotationCallback. rotation=$rotation")
            screenRotation = rotation
        }
    }

    private val sensorRotationCallback = object : SensorRotationCallback {
        override fun onRotation(orientation: Int, rotation: RotationEnum) {
            CsLogger.tag(TAG).d("sensorRotationCallback. rotation=$rotation")
            sensorRotation = rotation
        }
    }

}