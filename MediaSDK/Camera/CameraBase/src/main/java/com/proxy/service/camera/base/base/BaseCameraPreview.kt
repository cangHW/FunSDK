package com.proxy.service.camera.base.base

import android.view.Surface
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo

/**
 * @author: cangHX
 * @data: 2026/2/5 17:42
 * @desc:
 */
interface BaseCameraPreview : BaseCamera {

    /**
     * 设置预览 Surface
     * */
    fun setPreviewSurface(surface: Surface, mode: CameraMode)

    /**
     * 重新创建相机会话, 用于在配置发生变化后启用新配置
     * */
    fun reCreateCaptureSession()
}