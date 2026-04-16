package com.proxy.service.camera.base.constants

import com.proxy.service.camera.base.mode.loader.CameraAfMode
import com.proxy.service.camera.base.mode.view.CameraViewAfMode
import com.proxy.service.camera.base.mode.view.CameraViewMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:07
 * @desc:
 */
object CameraConstants {

    const val TAG = "Media_Camera_"

    val DEFAULT_VIEW_MODE: CameraViewMode = CameraViewMode.SURFACE_VIEW

    val DEFAULT_CAMERA_AF_MODE: CameraAfMode = CameraAfMode.AfAutoMode
    val DEFAULT_CAMERA_VIEW_AF_MODE: CameraViewAfMode = CameraViewAfMode.AfTouchMode()

}