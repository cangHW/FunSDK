package com.proxy.service.camera.base.loader.camera

import com.proxy.service.camera.base.mode.loader.CameraFaceMode

/**
 * @author: cangHX
 * @data: 2026/3/23 18:13
 * @desc:
 */
interface ICameraAction {

    /**
     * 开启相机
     * */
    fun openCamera(mode: CameraFaceMode)

    /**
     * 开始预览
     * */
    fun startPreview()

    /**
     * 恢复预览, 如果预览已经开始, 则将预览从暂停态变更为正常态. (如果配置了: LifecycleOwner, 默认跟随页面生命周期)
     * */
    fun resumePreview()

    /**
     * 暂停预览, 如果预览已经开始, 则将预览从正常态变更为暂停态. (如果配置了: LifecycleOwner, 默认跟随页面生命周期)
     * */
    fun pausePreview()

    /**
     * 停止预览
     * */
    fun stopPreview()

    /**
     * 释放相机
     * */
    fun releaseCamera()

}