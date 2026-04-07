package com.proxy.service.camera.base.view

import android.util.Size
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.callback.view.ITouchDispatch
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.CameraViewMode

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
     * 设置相机不同模式的预览尺寸, 来源 [CameraService.getSupportedPreviewSizes]
     *
     * @param mode      相机模式, 若为 null, 则代表全部模式都满足
     * @param faceMode  相机方向, 若为 null, 则代表全部方向都满足
     * @param size      预览尺寸
     * */
    fun setCameraPreviewSize(mode: CameraFunMode?, faceMode: CameraFaceMode?, size: Size): ICameraViewLoader

    /**
     * 设置相机不同模式的产物尺寸, 来源 [CameraService.getSupportedCaptureSizes]、[CameraService.getSupportedRecordSizes] 等
     *
     * @param mode      相机模式, 若为 null, 则代表全部模式都满足
     * @param faceMode  相机方向, 若为 null, 则代表全部方向都满足
     * @param size      产物尺寸
     * */
    fun setCameraOutSize(mode: CameraFunMode?, faceMode: CameraFaceMode?, size: Size): ICameraViewLoader

    /**
     * 设置默认摄像头, 默认 [CameraConstants.DEFAULT_CAMERA_FACE_MODE]
     * */
    fun setCameraFaceMode(mode: CameraFaceMode): ICameraViewLoader

    /**
     * 设置 view 模式, 默认 [CameraConstants.DEFAULT_VIEW_MODE]
     * */
    fun setCameraViewMode(mode: CameraViewMode): ICameraViewLoader

    /**
     * 设置相机对焦模式, 默认 [CameraConstants.DEFAULT_CAMERA_VIEW_AF_MODE]
     * */
    fun setCameraViewAfMode(mode: CameraViewAfMode): ICameraViewLoader

    /**
     * 创建相机 view 到 ViewGroup
     * */
    fun createTo(viewGroup: ViewGroup?): ICameraView

}