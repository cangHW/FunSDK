package com.proxy.service.apihttp.info.download.dispatcher.base

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.download.utils.ThreadUtils
import com.proxy.service.apihttp.info.download.worker.base.BaseStatusWorker
import com.proxy.service.apihttp.info.download.worker.base.IWorker
import com.proxy.service.core.service.task.CsTask

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

    /**
     * 最大同时下载数量
     * */
    protected var maxTaskCount: Int = 3
    protected val workerRunningList = ArrayList<IWorker>()
    protected var callback: OnWorkerIdleCallback? = null

    protected val taskWorkerFinishCallback = object : IWorker.TaskWorkerFinishCallback {
        override fun onFinished(worker: BaseStatusWorker, task: DownloadTask) {
            CsTask.launchTaskGroup(Config.TASK_LOOP_THREAD_NAME)?.start {
                workerRunningList.remove(worker)
                callback?.onWorkerIdle(task)
            }
        }
    }

    /**
     * 设置同时并发下载最大任务量
     * */
    fun setMaxDownloadTask(maxTaskCount: Int) {
        this.maxTaskCount = maxTaskCount
    }

    /**
     * 配置任务空闲回调
     * */
    fun setOnWorkerIdleCallback(callback: OnWorkerIdleCallback) {
        this.callback = callback
    }

    /**
     * 取消正在运行的任务
     * */
    fun cancelRunningTask(taskTag: String, isNeedCallback: Boolean) {
        ThreadUtils.checkCurrentThread()
        workerRunningList.forEach {
            it.cancelTask(taskTag, isNeedCallback)
        }
    }
}