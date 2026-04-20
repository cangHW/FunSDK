package com.proxy.service.camera.base.callback.loader

/**
 * @author: cangHX
 * @data: 2026/3/3 15:46
 * @desc:
 */
interface VideoRecordCallback {

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

}