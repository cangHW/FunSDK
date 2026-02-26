package com.proxy.service.camera.base.page

import android.content.Context
import com.proxy.service.camera.base.callback.PageTakePictureCallback
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:05
 * @desc:
 */
interface ICameraPageLoader {

    /**
     * 设置开启的相机模式, 默认全部开启
     * */
    fun setOpenCameraMode(list: List<CameraMode>): ICameraPageLoader

    /**
     * 设置默认的摄像头
     * */
    fun setDefaultCameraFaceMode(mode: CameraFaceMode): ICameraPageLoader

    /**
     * 设置拍照回调
     * */
    fun setTakePictureCallback(callback: PageTakePictureCallback): ICameraPageLoader

    /**
     * 启动支持横竖屏相机
     * */
    fun launch()

    /**
     * 启动竖屏相机
     * */
    fun launchPortrait()

    /**
     * 启动横屏相机
     * */
    fun launchLandscape()

    /**
     * 启动支持横竖屏相机
     * */
    fun launch(context: Context)

    /**
     * 启动竖屏相机
     * */
    fun launchPortrait(context: Context)

    /**
     * 启动横屏相机
     * */
    fun launchLandscape(context: Context)

}