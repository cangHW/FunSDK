package com.proxy.service.apihttp.info.download.controller.info.base

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.common.cache.GroupCache

/**
 * @author: cangHX
 * @data: 2024/11/6 10:06
 * @desc:
 */
abstract class BaseGroup : Comparator<DownloadTask> {

    protected val tag = "${Constants.LOG_DOWNLOAD_TAG_START}GroupInfo"

    protected val groupCache = GroupCache<DownloadTask> { o1, o2 ->
        o1.getPriority().compareTo(o2.getPriority())
    }
//    protected val waitingTasks = ArrayList<DownloadTask>()
//    protected val runningTasks = ArrayList<DownloadTask>()

    /**
     * 获取第一个任务
     * */
    fun getFirstDownloadTaskOnWaiting(): DownloadTask? {
        return groupCache.getFirstOrNullWaitingValue()
    }

    /**
     * 获取等待任务数量
     * */
    fun getWaitingTaskNum(): Int {
        return groupCache.waitingSize()
    }

    /**
     * 获取全部任务数量
     * */
    fun getAllTaskNum(): Int {
        return groupCache.allSize()
    }

    /**
     * 获取全部任务
     * */
    fun getAllTask(): List<DownloadTask> {
        return groupCache.getAllValue()
    }

    override fun compare(o1: DownloadTask?, o2: DownloadTask?): Int {
        return (o1?.getPriority() ?: 0).compareTo(o2?.getPriority() ?: 0)
    }

}