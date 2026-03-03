package com.proxy.service.camera.base.base

import com.proxy.service.camera.base.callback.VideoRecordCallback

/**
 * @author: cangHX
 * @data: 2026/3/3 10:43
 * @desc:
 */
interface BaseCameraRecord {

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