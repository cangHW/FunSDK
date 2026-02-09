package com.proxy.service.camera.base.loader

import android.view.Surface
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.base.BaseCameraAction
import com.proxy.service.camera.base.mode.CameraAfMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:02
 * @desc:
 */
interface ICameraLoader : BaseCameraAction {

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner)

    /**
     * 设置相机对焦模式
     * */
    fun setCameraAfMode(mode: CameraAfMode)

    /**
     * 更新拍照预览
     * */
    fun setPicturePreview(surface: Surface, width: Int, height: Int)

    /**
     * 更新录像预览
     * */
    fun setVideoPreview(surface: Surface)

}