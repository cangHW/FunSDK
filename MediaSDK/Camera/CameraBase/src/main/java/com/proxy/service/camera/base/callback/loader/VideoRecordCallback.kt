package com.proxy.service.camera.base.callback.loader

import com.proxy.service.camera.base.mode.loader.VideoRecordState

/**
 * @author: cangHX
 * @data: 2026/3/3 15:46
 * @desc:
 */
interface VideoRecordCallback {

    /**
     * 录制状态变化
     * */
    fun onVideoRecordStateChanged(state: VideoRecordState) {}

    /**
     * 成功
     *
     * @param filePath 视频地址
     * */
    fun onVideoRecordSuccess(filePath: String)

    /**
     * 失败
     * */
    fun onVideoRecordFailed()

    /**
     * 取消
     * */
    fun onVideoRecordCancel()
}