package com.proxy.service.camera.info.view.config

import android.util.Size
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode

/**
 * @author: cangHX
 * @data: 2026/3/3 17:30
 * @desc:
 */

abstract class UserSize(val mode: CameraFunMode?, val faceMode: CameraFaceMode?, val size: Size)

class UserPreviewSize(
    mode: CameraFunMode?,
    faceMode: CameraFaceMode?,
    size: Size
) : UserSize(mode, faceMode, size)

class UserOutSize(
    mode: CameraFunMode?,
    faceMode: CameraFaceMode?,
    size: Size
) : UserSize(mode, faceMode, size)