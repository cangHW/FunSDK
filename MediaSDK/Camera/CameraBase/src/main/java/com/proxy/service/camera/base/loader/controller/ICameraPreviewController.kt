package com.proxy.service.camera.base.loader.controller

import com.proxy.service.camera.base.mode.loader.PreviewImageFormatMode

/**
 * @author: cangHX
 * @data: 2026/4/20 11:37
 * @desc:
 */
interface ICameraPreviewController : ICameraController {

    /**
     * 设置预览回调的数据图像格式
     * */
    fun setImageFormatMode(format: PreviewImageFormatMode)

}