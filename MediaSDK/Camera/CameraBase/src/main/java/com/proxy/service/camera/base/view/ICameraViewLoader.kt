package com.proxy.service.camera.base.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.callback.view.ITouchDispatch
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.view.CameraViewMeteringMode
import com.proxy.service.camera.base.mode.view.CameraViewMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:04
 * @desc:
 */
interface ICameraViewLoader {

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): ICameraViewLoader

    /**
     * 设置自定义事件分发
     * */
    fun setCustomTouchDispatch(touchDispatch: ITouchDispatch): ICameraViewLoader

    /**
     * 设置默认摄像头, 默认为 null
     * */
    fun setCameraFaceMode(mode: CameraFaceMode): ICameraViewLoader

    /**
     * 设置 view 模式, 默认 [CameraConstants.DEFAULT_VIEW_MODE]
     * */
    fun setCameraViewMode(mode: CameraViewMode): ICameraViewLoader

    /**
     * 设置相机测光模式, 默认 [CameraConstants.DEFAULT_CAMERA_VIEW_METERING_MODE]
     * */
    fun setCameraViewMeteringMode(mode: CameraViewMeteringMode): ICameraViewLoader

    /**
     * 设置触摸测光框是否显示, 默认 true
     * */
    fun setCameraViewMeteringRectVisible(visible: Boolean): ICameraViewLoader

    /**
     * 创建相机 view 到 ViewGroup
     * */
    fun createTo(viewGroup: ViewGroup?): ICameraView

}