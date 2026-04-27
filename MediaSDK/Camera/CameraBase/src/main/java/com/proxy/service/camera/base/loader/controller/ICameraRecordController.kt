package com.proxy.service.camera.base.loader.controller

import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.mode.loader.AudioEncoderMode
import com.proxy.service.camera.base.mode.loader.VideoEncoderMode
import com.proxy.service.camera.base.mode.loader.VideoRecordState

/**
 * @author: cangHX
 * @data: 2026/3/19 19:17
 * @desc:
 */
interface ICameraRecordController : ICameraController {

    /**
     * 设置视频编码格式
     * */
    fun setVideoEncoder(mode: VideoEncoderMode)

    /**
     * 设置视频帧率
     * */
    fun setVideoFrameRate(rate: Int)

    /**
     * 设置视频比特率
     * */
    fun setVideoEncodingBitRate(bitRate: Int)


    /**
     * 设置音频是否可用
     * */
    fun setAudioEnabled(enabled: Boolean)

    /**
     * 设置音频编码格式
     * */
    fun setAudioEncoder(mode: AudioEncoderMode)

    /**
     * 设置音频帧率
     * */
    fun setAudioSamplingRate(samplingRate: Int)

    /**
     * 设置音频比特率
     * */
    fun setAudioEncodingBitRate(bitRate: Int)

    /**
     * 设置音频比特率
     * */
    fun setAudioChannels(channels: Int)


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