package com.proxy.service.apihttp.base.download.callback

import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.download.task.DownloadTask

/**
 * @author: cangHX
 * @data: 2024/10/31 11:40
 * @desc:
 */
interface DownloadCallback {

    /**
     * 任务已添加，但是没有下载资源，等待开始任务
     *
     * @param task  任务信息
     * */
    fun onWaiting(task: DownloadTask)

    /**
     * 开始任务
     *
     * @param task  任务信息
     * */
    fun onStart(task: DownloadTask)

    /**
     * 下载中
     *
     * @param task          任务信息
     * @param currentSize   已下载大小, 单位: B
     * @param progress      百分比进度，保留两位小数
     * @param speed         下载速度, 单位: B/S
     *
     * */
    fun onProgress(task: DownloadTask, currentSize: Long, progress: Float, speed: Long)

    /**
     * 下载成功
     * @param task  任务信息
     * */
    fun onSuccess(task: DownloadTask)

    /**
     * 下载取消
     * @param task  任务信息
     * */
    fun onCancel(task: DownloadTask)

    /**
     * 下载失败
     * @param task      任务信息
     * @param exception 错误信息
     * */
    fun onFailed(task: DownloadTask, exception: DownloadException)
}