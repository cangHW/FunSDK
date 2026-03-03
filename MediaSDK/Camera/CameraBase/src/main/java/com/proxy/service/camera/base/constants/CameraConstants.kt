package com.proxy.service.camera.base.constants

import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.ViewMode
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:07
 * @desc:
 */
object CameraConstants {

    const val TAG = "Media_Camera_"

    val DEFAULT_CAMERA_MODE: CameraMode = CameraMode.CAPTURE

    val DEFAULT_CAMERA_FACE_MODE: CameraFaceMode = CameraFaceMode.FaceBack

    val DEFAULT_VIEW_MODE: ViewMode = ViewMode.SURFACE_VIEW

    val DEFAULT_CAMERA_AF_MODE: CameraAfMode = CameraAfMode.AfAutoMode
    val DEFAULT_CAMERA_VIEW_AF_MODE: CameraViewAfMode = CameraViewAfMode.AfTouchMode()

}