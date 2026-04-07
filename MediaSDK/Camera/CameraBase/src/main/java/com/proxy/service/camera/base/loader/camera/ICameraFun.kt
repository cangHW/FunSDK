package com.proxy.service.camera.base.loader.camera

import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController

/**
 * @author: cangHX
 * @data: 2026/3/23 18:13
 * @desc:
 */
interface ICameraFun {

    /**
     * 选择拍照模式
     * */
    fun chooseCaptureMode(): ICameraCaptureController

    /**
     * 选择录像模式
     * */
    fun chooseRecordMode(): ICameraRecordController

}