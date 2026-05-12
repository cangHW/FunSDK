package com.proxy.service.apihttp.info.download.dispatcher.base

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.common.cache.MaxCache
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.download.worker.base.IWorker

/**
 * @author: cangHX
 * @date: 2024/11/5 20:24
 * @desc:
 */
abstract class BaseDispatcher {

    protected val tag = "${ApiConstants.LOG_DOWNLOAD_TAG_START}TaskDispatcher"

    interface OnWorkerIdleCallback {
        fun onWorkerIdle(task: DownloadTask?)
    }

    protected var callback: OnWorkerIdleCallback? = null
    protected val runningWorkers = MaxCache<IWorker>(Config.maxDownloadTaskCount)

    protected val taskWorkerFinishCallback = object : IWorker.TaskWorkerFinishCallback {
        override fun onFinished(worker: IWorker, task: DownloadTask) {
            runningWorkers.remove(worker)
            callback?.onWorkerIdle(task)
        }
    }

    /**
     * 配置任务空闲回调
     * */
    fun setOnWorkerIdleCallback(callback: OnWorkerIdleCallback) {
        this.callback = callback
    }

    /**
     * 任务是否已经满了
     * */
    fun isTaskFull(): Boolean {
        return runningWorkers.isFull()
    }

    /**
     * 取消正在运行的任务
     * */
    fun cancelRunningTask(taskTag: String, isNeedCallback: Boolean) {
        runningWorkers.getAllCache().forEach {
            it.cancelTask(taskTag, isNeedCallback)
        }
    }
}