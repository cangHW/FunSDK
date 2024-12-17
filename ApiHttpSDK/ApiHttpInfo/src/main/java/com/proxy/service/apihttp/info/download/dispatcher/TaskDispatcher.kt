package com.proxy.service.apihttp.info.download.dispatcher

import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.download.controller.TaskController
import com.proxy.service.apihttp.info.download.dispatcher.base.BaseDispatcher
import com.proxy.service.apihttp.info.download.utils.ThreadUtils
import com.proxy.service.apihttp.info.download.worker.MultiTaskWorker
import com.proxy.service.apihttp.info.download.worker.SingleTaskWorker
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2024/10/31 11:32
 * @desc:
 */
object TaskDispatcher : BaseDispatcher() {

    /**
     * 任务是否已经满了
     * */
    fun isTaskFull(): Boolean {
        return workerRunningList.size >= maxTaskCount
    }

    /**
     * 发起一个任务并确认当前资源是否允许执行
     * */
    fun startTask(task: DownloadTask): Boolean {
        ThreadUtils.checkCurrentThread()
        if (isTaskFull()) {
            return false
        }
        val worker = if (task.getMultiPartEnable()) {
            MultiTaskWorker(task)
        } else {
            SingleTaskWorker(task)
        }
        worker.setOnFinishedCallback(taskWorkerFinishCallback)
        workerRunningList.add(worker)
        worker.startTask()
        return true
    }

    /**
     * 重置全部正在运行的任务, 用于快速执行高优先级任务
     * */
    fun resetAllRunningTask() {
        CsTask.launchTaskGroup(Config.TASK_LOOP_THREAD_NAME)?.start {
            CsLogger.tag(tag).i("重置全部正在运行的任务 taskNum = ${workerRunningList.size}")
            workerRunningList.forEach {
                CsLogger.tag(tag).i("准备重置任务 taskTag = ${it.getDownloadTask().getTaskTag()}")
                cancelRunningTask(it.getDownloadTask().getTaskTag(), false)
                TaskController.addTask(it.getDownloadTask())
            }
        }
    }
}