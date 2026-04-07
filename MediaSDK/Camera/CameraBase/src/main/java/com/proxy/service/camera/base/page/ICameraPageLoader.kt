package com.proxy.service.camera.base.page

import android.content.Context
import com.proxy.service.camera.base.callback.PagePictureCaptureCallback
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:05
 * @desc:
 */
interface ICameraPageLoader {

    /**
     * 设置支持的相机模式, 默认全部支持
     * */
    fun setOpenCameraMode(list: List<CameraFunMode>): ICameraPageLoader

    /**
     * 设置默认的摄像头
     * */
    fun setDefaultCameraFaceMode(mode: CameraFaceMode): ICameraPageLoader

    /**
     * 设置拍照回调
     * */
    fun setTakePictureCallback(callback: PagePictureCaptureCallback): ICameraPageLoader

    /**
     * 启动支持横竖屏相机
     * */
    fun launch(context: Context? = null)

    /**
     * 启动竖屏相机
     * */
    fun launchPortrait(context: Context? = null)

    /**
     * 启动横屏相机
     * */
    fun launchLandscape(context: Context? = null)

}