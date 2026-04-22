package com.proxy.service.camera.base.loader.controller

/**
 * @author: cangHX
 * @data: 2026/4/20 11:17
 * @desc:
 */
interface ICameraController {

    /**
     * 设置旋转角度. [0、90、180、270]
     * */
    fun setSurfaceOrientation(degrees:Int)

    /**
     * 设置尺寸
     * */
    fun setSurfaceSize(width: Int, height: Int)

}