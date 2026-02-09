package com.proxy.service.camera.base.callback

import java.io.Serializable

/**
 * @author: cangHX
 * @data: 2026/2/8 17:55
 * @desc:
 */
interface PageTakePictureCallback : Serializable {

    fun onTakePictureSuccess(path: String)

    fun onTakePictureFailed()
}