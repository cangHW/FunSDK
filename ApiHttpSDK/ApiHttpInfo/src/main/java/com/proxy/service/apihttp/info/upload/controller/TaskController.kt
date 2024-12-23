package com.proxy.service.apihttp.info.upload.controller

import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.upload.controller.base.BaseTaskController
import com.proxy.service.apihttp.info.upload.dispatcher.TaskDispatcher
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2024/12/18 10:37
 * @desc:
 */
object TaskController : BaseTaskController() {

    /**
     * 添加一个新任务
     * */
    fun addTask(task: UploadTask) {
        CsTask.launchTaskGroup(Config.UPLOAD_DISPATCHER_THREAD_NAME)?.start {
            tasks.tryAdd(task)
            tryStartTask()
        }
    }

    /**
     * 取消任务
     * */
    fun cancel(taskTag: String) {
        CsTask.launchTaskGroup(Config.UPLOAD_DISPATCHER_THREAD_NAME)?.start {
            tasks.getAllCache().forEach {
                if (it.getTaskTag() == taskTag) {
                    tasks.remove(it)
                }
            }

            TaskDispatcher.cancel(taskTag)
        }
    }

    /**
     * 取消全部任务
     * */
    fun cancelAll() {
        CsTask.launchTaskGroup(Config.UPLOAD_DISPATCHER_THREAD_NAME)?.start {
            tasks.clear()
            TaskDispatcher.cancelAll()
        }
    }
}