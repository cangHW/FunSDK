package com.proxy.service.camera.info.page.params

import com.proxy.service.camera.base.callback.PageTakePictureCallback
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode

/**
 * @author: cangHX
 * @data: 2026/2/8 18:00
 * @desc:
 */
class MediaCameraParams {

    /**
     * 支持的相机模式
     * */
    val supportCameraModes = ArrayList<CameraMode>().apply {
        addAll(CameraMode.getAll())
    }

    /**
     * 默认摄像头
     * */
    var defaultCameraFaceMode: CameraFaceMode = CameraFaceMode.FaceBack


    /**
     * 拍照回调
     * */
    var takePictureCallback: PageTakePictureCallback? = null

}