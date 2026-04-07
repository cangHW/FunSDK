package com.proxy.service.camera.base.loader.camera

import com.proxy.service.camera.base.mode.CameraFunMode

/**
 * @author: cangHX
 * @data: 2026/3/30 15:07
 * @desc:
 */
interface ICameraFunGet {

    /**
     * 获取当前选择的相机模式
     * */
    fun getChooseMode(): CameraFunMode?
}