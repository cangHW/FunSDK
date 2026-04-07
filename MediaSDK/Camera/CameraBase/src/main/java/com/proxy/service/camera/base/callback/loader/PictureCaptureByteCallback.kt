package com.proxy.service.camera.base.callback.loader

/**
 * @author: cangHX
 * @data: 2026/2/5 15:33
 * @desc:
 */
interface PictureCaptureByteCallback {

    /**
     * 成功
     *
     * @param bytes 图片内容
     * */
    fun onPictureCaptureSuccess(bytes: ByteArray)

    /**
     * 失败
     * */
    fun onPictureCaptureFailed()

}