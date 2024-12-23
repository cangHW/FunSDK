package com.proxy.service.apihttp.info.upload.controller.base

import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.common.cache.Cache
import com.proxy.service.apihttp.info.upload.dispatcher.TaskDispatcher
import com.proxy.service.apihttp.info.upload.dispatcher.base.BaseDispatcher.OnWorkerIdleCallback
import com.proxy.service.apihttp.info.upload.utils.ThreadUtils

/**
 * @author: cangHX
 * @data: 2024/12/19 18:20
 * @desc:
 */
abstract class BaseTaskController {

    protected val tasks = Cache<UploadTask>(Int.MAX_VALUE)

    private val onWorkerIdleCallback = object : OnWorkerIdleCallback {
        override fun onWorkerIdle() {
            tryStartTask()
        }
    }

    init {
        TaskDispatcher.setOnWorkerIdleCallback(onWorkerIdleCallback)
    }

    /**
     * 尝试发起一个任务
     * */
    protected fun tryStartTask() {
        ThreadUtils.checkCurrentThread()
        tasks.getOrNull(0)?.let {
            if (TaskDispatcher.tryStartTask(it)) {
                tasks.remove(it)
            }
        }
    }

}