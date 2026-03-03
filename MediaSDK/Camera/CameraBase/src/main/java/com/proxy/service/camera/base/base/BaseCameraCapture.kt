package com.proxy.service.camera.base.base

import com.proxy.service.camera.base.callback.TakePictureCallback

/**
 * @author: cangHX
 * @data: 2026/3/3 10:43
 * @desc:
 */
interface BaseCameraCapture {

    /**
     * 设置拍照图片尺寸
     * */
    fun setPictureCaptureSize(width: Int, height: Int)

    /**
     * 拍照
     * */
    fun startPictureCapture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?)

}