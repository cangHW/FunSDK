package com.proxy.service.apihttp.info.download.dispatcher.base

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.common.cache.MaxCache
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.download.worker.base.BaseStatusWorker
import com.proxy.service.apihttp.info.download.worker.base.IWorker

/**
 * @author: cangHX
 * @data: 2024/11/5 20:24
 * @desc:
 */
abstract class BaseDispatcher {

    protected val tag = "${Constants.LOG_DOWNLOAD_TAG_START}TaskDispatcher"

    interface OnWorkerIdleCallback {
        fun onWorkerIdle(task: DownloadTask?)
    }

    protected var callback: OnWorkerIdleCallback? = null
    protected val workerRunningList = MaxCache<IWorker>(Config.maxDownloadTaskCount)

    protected val taskWorkerFinishCallback = object : IWorker.TaskWorkerFinishCallback {
        override fun onFinished(worker: BaseStatusWorker, task: DownloadTask) {
            workerRunningList.remove(worker)
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
        return workerRunningList.isFull()
    }

    /**
     * 取消正在运行的任务
     * */
    fun cancelRunningTask(taskTag: String, isNeedCallback: Boolean) {
        workerRunningList.getAllCache().forEach {
            it.cancelTask(taskTag, isNeedCallback)
        }
    }
}