package com.proxy.service.apihttp.info.upload.dispatcher.base

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.common.cache.MaxCache
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.upload.worker.base.IWorker
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2024/12/18 11:18
 * @desc:
 */
abstract class BaseDispatcher {

    protected val tag = "${ApiConstants.LOG_UPLOAD_TAG_START}TaskDispatcher"

    interface OnWorkerIdleCallback {
        fun onWorkerIdle()
    }

    protected var workers: MaxCache<IWorker> = MaxCache(Config.maxUploadTaskCount)
    protected var workerIdleCallback: OnWorkerIdleCallback? = null

    protected val taskWorkerFinishCallback = object : IWorker.TaskWorkerFinishCallback {
        override fun onFinished(worker: IWorker, task: UploadTask) {
            CsTask.launchTaskGroup(Config.UPLOAD_DISPATCHER_THREAD_NAME)?.start {
                workers.remove(worker)
                workerIdleCallback?.onWorkerIdle()
            }
        }
    }

    /**
     * 设置任务空闲回调
     * */
    fun setOnWorkerIdleCallback(callback: OnWorkerIdleCallback) {
        this.workerIdleCallback = callback
    }
}