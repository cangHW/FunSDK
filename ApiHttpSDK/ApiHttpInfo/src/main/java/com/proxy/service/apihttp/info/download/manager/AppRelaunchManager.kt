package com.proxy.service.apihttp.info.download.manager

import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.info.download.controller.TaskController
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/15 17:01
 * @desc:
 */
object AppRelaunchManager {

    fun reResumeTask(enable: Boolean) {
        if (!enable) {
            return
        }
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                DownloadRoom.INSTANCE.getTaskDao()
                    .queryTasksByStatusUpTo(StatusEnum.PROGRESS.status).forEach {
                        TaskController.addTask(it.getDownloadTask())
                    }
                return ""
            }
        })?.start()
    }

}