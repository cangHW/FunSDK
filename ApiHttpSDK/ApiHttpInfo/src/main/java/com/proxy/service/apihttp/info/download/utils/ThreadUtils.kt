package com.proxy.service.apihttp.info.download.utils

import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2024/11/7 10:45
 * @desc:
 */
object ThreadUtils {

    /**
     * 检查当前任务是否运行在规定线程里面
     * */
    fun checkCurrentThread() {
        if (
            Thread.currentThread().id
            !=
            CsTask.launchTaskGroup(Config.DOWNLOAD_DISPATCHER_THREAD_NAME)?.getThreadId()
        ) {
            throw IllegalArgumentException("The current thread is not the default thread. DefaultThreadName = ${Config.DOWNLOAD_DISPATCHER_THREAD_NAME}")
        }
    }

}