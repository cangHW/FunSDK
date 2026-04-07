package com.proxy.service.camera.base.callback

/**
 * @author: cangHX
 * @data: 2026/2/8 17:55
 * @desc:
 */
interface PagePictureCaptureCallback {

    /**
     * 拍照成功
     * */
    fun onPictureCaptureSuccess(path: String)

    /**
     * 拍照失败
     * */
    fun onPictureCaptureFailed()
}