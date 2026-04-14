package com.proxy.service.camera.base.page

import android.content.Context
import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
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
    fun setSupportCameraFunMode(list: List<CameraFunMode>): ICameraPageLoader

    /**
     * 设置默认的摄像头
     * */
    fun setDefaultCameraFaceMode(mode: CameraFaceMode): ICameraPageLoader

    /**
     * 设置拍照回调
     * */
    fun setPictureCaptureCallback(callback: PictureCaptureCallback): ICameraPageLoader

    /**
     * 设置拍照并保存相册回调
     * */
    fun setPictureCaptureToAlbumCallback(callback: PictureCaptureCallback): ICameraPageLoader

    /**
     * 设置拍照回调
     *
     * @param filePath      照片文件待保存地址. 为空则使用默认文件夹
     * @param callback      数据回调
     * */
    fun setPictureCaptureCallback(
        filePath: String,
        callback: PictureCaptureCallback
    ): ICameraPageLoader

    /**
     * 设置拍照回调, 字节
     *
     * @param callback      数据回调
     * */
    fun setPictureCaptureCallback(callback: PictureCaptureByteCallback): ICameraPageLoader

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