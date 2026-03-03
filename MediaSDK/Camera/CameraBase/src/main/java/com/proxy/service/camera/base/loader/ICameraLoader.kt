package com.proxy.service.camera.base.loader

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.base.BaseCameraPreview
import com.proxy.service.camera.base.base.BaseCameraCapture
import com.proxy.service.camera.base.base.BaseCameraRecord
import com.proxy.service.camera.base.mode.CameraAfMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:02
 * @desc:
 */
interface ICameraLoader : BaseCameraPreview, BaseCameraCapture, BaseCameraRecord {

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner)

    /**
     * 设置相机对焦模式
     * */
    fun setCameraAfMode(mode: CameraAfMode)

}