package com.proxy.service.camera.info.page.params

import com.proxy.service.camera.base.callback.PagePictureCaptureCallback
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode

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
     * 拍照回调
     * */
    var pictureCaptureCallback: PagePictureCaptureCallback? = null

}