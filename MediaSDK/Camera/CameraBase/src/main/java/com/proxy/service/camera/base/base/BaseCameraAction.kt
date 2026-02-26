package com.proxy.service.camera.base.base

import com.proxy.service.camera.base.callback.TakePictureCallback

/**
 * @author: cangHX
 * @data: 2026/2/5 17:42
 * @desc:
 */
interface BaseCameraAction : BaseCamera {

    /**
     * 拍照
     * */
    fun takePicture(isSavePhotoAlbum: Boolean, callback: TakePictureCallback?)
}