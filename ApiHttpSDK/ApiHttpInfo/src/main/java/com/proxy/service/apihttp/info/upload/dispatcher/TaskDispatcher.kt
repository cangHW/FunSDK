package com.proxy.service.apihttp.info.upload.dispatcher

import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.upload.dispatcher.base.BaseDispatcher
import com.proxy.service.apihttp.info.upload.utils.ThreadUtils
import com.proxy.service.apihttp.info.upload.worker.TaskWorker

/**
 * @author: cangHX
 * @data: 2024/12/18 11:07
 * @desc:
 */
object TaskDispatcher : BaseDispatcher() {

    /**
     * 尝试开始一个任务
     * */
    fun tryStartTask(task: UploadTask): Boolean {
        ThreadUtils.checkCurrentThread()
        if (workers.isFull()) {
            return false
        }

        val worker = TaskWorker(task)
        if (!workers.tryAdd(worker)) {
            return false
        }

        worker.setOnFinishedCallback(taskWorkerFinishCallback)
        worker.startTask()
        return true
    }

    /**
     * 取消任务
     * */
    fun cancel(taskTag: String) {
        ThreadUtils.checkCurrentThread()
        workers.getAllCache().forEach {
            it.cancelTask(taskTag)
        }
    }

    /**
     * 取消任务
     * */
    fun cancelAll() {
        ThreadUtils.checkCurrentThread()
        workers.getAllCache().forEach {
            it.cancelTask(it.getUploadTask().getTaskTag())
        }
    }
}