package com.proxy.service.apihttp.info.download.worker.base

import com.proxy.service.apihttp.base.download.task.DownloadTask

/**
 * @author: cangHX
 * @data: 2024/11/13 11:18
 * @desc:
 */
interface IWorker {

    interface TaskWorkerFinishCallback {
        fun onFinished(worker: BaseStatusWorker, task: DownloadTask)
    }

    /**
     * 配置任务完成回调
     * */
    fun setOnFinishedCallback(callback: IWorker.TaskWorkerFinishCallback)

    /**
     * 开始任务
     * */
    fun startTask()

    /**
     * 取消任务
     *
     * @param isNeedCallback    是否需要回调任务取消
     * */
    fun cancelTask(taskTag: String, isNeedCallback: Boolean)

    /**
     * 获取当前任务绑定的 DownloadTask
     * */
    fun getDownloadTask():DownloadTask
}