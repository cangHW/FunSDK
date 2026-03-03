package com.proxy.service.camera.base.config.view

import android.util.Size
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode

/**
 * @author: cangHX
 * @data: 2026/3/3 17:30
 * @desc:
 */

abstract class UserSize(val mode: CameraMode?, val faceMode: CameraFaceMode?, val size: Size)

class UserPreviewSize(
    mode: CameraMode?,
    faceMode: CameraFaceMode?,
    size: Size
) : UserSize(mode, faceMode, size)

class UserOutSize(
    mode: CameraMode?,
    faceMode: CameraFaceMode?,
    size: Size
) : UserSize(mode, faceMode, size)