package com.proxy.service.apihttp.base.upload.callback

import com.proxy.service.apihttp.base.upload.task.UploadTask

/**
 * @author: cangHX
 * @data: 2024/12/18 09:55
 * @desc:
 */
interface UploadCallback {

    /**
     * 任务已添加，但是没有上传资源，等待开始任务
     *
     * @param task  任务信息
     * */
    fun onWaiting(task: UploadTask)

    /**
     * 开始任务
     *
     * @param task  任务信息
     * */
    fun onStart(task: UploadTask)

    /**
     * 上传成功
     * @param task  任务信息
     * */
    fun onSuccess(task: UploadTask)

    /**
     * 上传取消
     * @param task  任务信息
     * */
    fun onCancel(task: UploadTask)

    /**
     * 上传失败
     * @param task      任务信息
     * @param throwable 错误信息
     * */
    fun onFailed(task: UploadTask, throwable: Throwable)
}