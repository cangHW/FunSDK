package com.proxy.service.camera.info.view.config

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.view.CameraViewMeteringMode

/**
 * @author: cangHX
 * @data: 2026/3/27 18:37
 * @desc:
 */
class CameraViewConfig {

    var lifecycleOwner: LifecycleOwner? = null

    var cameraFaceMode: CameraFaceMode? = null
    var cameraViewMeteringMode: CameraViewMeteringMode = CameraConstants.DEFAULT_CAMERA_VIEW_METERING_MODE
    var cameraViewMeteringRectVisible: Boolean = true

}