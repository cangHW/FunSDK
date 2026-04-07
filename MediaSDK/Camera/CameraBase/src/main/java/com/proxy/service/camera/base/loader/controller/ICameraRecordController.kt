package com.proxy.service.camera.base.loader.controller

import com.proxy.service.camera.base.callback.loader.VideoRecordCallback

/**
 * @author: cangHX
 * @data: 2026/3/19 19:17
 * @desc:
 */
interface ICameraRecordController {

    /**
     * 设置录制视频尺寸
     * */
    fun setVideoRecordSize(width: Int, height: Int)

    /**
     * 开始录制
     * */
    fun startVideoRecording(isSavePhotoAlbum: Boolean, callback: VideoRecordCallback?)

    /**
     * 结束录制
     * */
    fun finishVideoRecording()
}