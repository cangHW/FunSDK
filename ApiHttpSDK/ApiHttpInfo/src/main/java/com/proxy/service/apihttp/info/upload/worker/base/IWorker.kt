package com.proxy.service.apihttp.info.upload.worker.base

import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.upload.worker.TaskWorker

/**
 * @author: cangHX
 * @data: 2024/12/18 11:13
 * @desc:
 */
interface IWorker {

    interface TaskWorkerFinishCallback {
        fun onFinished(worker: IWorker, task: UploadTask)
    }

    /**
     * 配置任务完成回调
     * */
    fun setOnFinishedCallback(callback: TaskWorkerFinishCallback)

    /**
     * 开始任务
     * */
    fun startTask()

    /**
     * 取消任务
     * */
    fun cancelTask(taskTag: String): Boolean

    /**
     * 获取当前任务绑定的 UploadTask
     * */
    fun getUploadTask(): UploadTask
}