package com.proxy.service.camera.base.callback.loader

/**
 * @author: cangHX
 * @data: 2026/2/5 15:33
 * @desc:
 */
interface PictureCaptureCallback {

    /**
     * 成功
     *
     * @param filePath 图片地址
     * */
    fun onPictureCaptureSuccess(filePath: String)

    /**
     * 失败
     * */
    fun onPictureCaptureFailed()

}