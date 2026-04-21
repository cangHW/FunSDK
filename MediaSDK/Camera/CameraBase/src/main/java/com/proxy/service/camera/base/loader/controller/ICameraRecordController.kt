package com.proxy.service.camera.base.loader.controller

import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.mode.loader.VideoRecordState

/**
 * @author: cangHX
 * @data: 2026/3/19 19:17
 * @desc:
 */
interface ICameraRecordController : ICameraController {

    /**
     * 获取当前录制状态
     * */
    fun getVideoRecordState(): VideoRecordState

    /**
     * 开始录制
     * */
    fun startVideoRecording(callback: VideoRecordCallback?)

    /**
     * 开始录制
     * */
    fun startVideoRecordingToLocal(filePath: String, callback: VideoRecordCallback?)

    /**
     * 开始录制
     * */
    fun startVideoRecordingToAlbum(callback: VideoRecordCallback?)

    /**
     * 结束录制
     * */
    fun finishVideoRecording()
}