package com.proxy.service.camera.base.callback

/**
 * @author: cangHX
 * @data: 2026/2/8 17:55
 * @desc:
 */
interface PageTakePictureCallback {

    /**
     * 拍照成功
     * */
    fun onTakePictureSuccess(path: String)

    /**
     * 拍照失败
     * */
    fun onTakePictureFailed()
}