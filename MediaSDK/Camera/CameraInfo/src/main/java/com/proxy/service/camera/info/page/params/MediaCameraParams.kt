package com.proxy.service.camera.info.page.params

import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode

/**
 * @author: cangHX
 * @data: 2026/2/8 18:00
 * @desc:
 */
class MediaCameraParams {

    /**
     * 支持的相机模式
     * */
    val supportCameraFunModes = ArrayList<CameraFunMode>().apply {
        addAll(CameraFunMode.getAll())
    }

    /**
     * 默认摄像头
     * */
    var defaultCameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack

    /**
     * 拍照文件地址
     * */
    var filePath: String? = null

    /**
     * 拍照文件保存到相册
     * */
    var isSaveAlbum: Boolean = false

    /**
     * 拍照回调
     * */
    var pictureCaptureCallback: PictureCaptureCallback? = null

    /**
     * 拍照回调
     * */
    var pictureCaptureByteCallback: PictureCaptureByteCallback? = null

}