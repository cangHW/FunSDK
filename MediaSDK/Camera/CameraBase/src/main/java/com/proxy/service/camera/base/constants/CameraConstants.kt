package com.proxy.service.camera.base.constants

import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraViewMode
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.base.mode.CameraViewAfMode

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