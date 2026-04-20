package com.proxy.service.camera.base.loader.controller

import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback

/**
 * @author: cangHX
 * @data: 2026/3/19 19:13
 * @desc:
 */
interface ICameraCaptureController : ICameraController {

    /**
     * 拍照并获取图片字节信息
     * */
    fun startPictureCapture(callback: PictureCaptureByteCallback?)

    /**
     * 拍照并保存到本地
     * */
    fun startPictureCaptureToLocal(callback: PictureCaptureCallback?)

    /**
     * 拍照并保存到指定位置
     * */
    fun startPictureCaptureToLocal(filePath: String, callback: PictureCaptureCallback?)

    /**
     * 拍照并保存到相册
     * */
    fun startPictureCaptureToAlbum(callback: PictureCaptureCallback?)

}