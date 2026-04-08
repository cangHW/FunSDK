package com.proxy.service.camera.info.view.config

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraViewAfMode

/**
 * @author: cangHX
 * @data: 2026/3/27 18:37
 * @desc:
 */
class CameraViewConfig {

    var lifecycleOwner: LifecycleOwner? = null

    var cameraFaceMode: CameraFaceMode? = null
    var cameraViewAfMode: CameraViewAfMode = CameraConstants.DEFAULT_CAMERA_VIEW_AF_MODE

}