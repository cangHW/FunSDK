package com.proxy.service.apihttp.info.download.controller.info.base

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.DownloadTask

/**
 * @author: cangHX
 * @data: 2024/11/6 10:06
 * @desc:
 */
abstract class BaseGroup : Comparator<DownloadTask> {

    protected val tag = "${Constants.LOG_DOWNLOAD_TAG_START}GroupInfo"

    protected val tasks = ArrayList<DownloadTask>()

    /**
     * 获取第一个任务
     * */
    fun getFirstDownloadTask(): DownloadTask? {
        return tasks.firstOrNull()
    }

    /**
     * 获取任务数量
     * */
    fun getTaskNum(): Int {
        return tasks.size
    }

    /**
     * 获取全部任务
     * */
    fun getAllTask(): List<DownloadTask> {
        return ArrayList<DownloadTask>(tasks)
    }

    override fun compare(o1: DownloadTask?, o2: DownloadTask?): Int {
        return (o1?.getPriority() ?: 0).compareTo(o2?.getPriority() ?: 0)
    }

}