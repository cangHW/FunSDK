package com.proxy.service.threadpool.info.handler.loader

import com.proxy.service.threadpool.base.handler.controller.ITaskDisposable
import com.proxy.service.threadpool.base.handler.loader.IHandlerLoader
import com.proxy.service.threadpool.info.handler.controller.TaskDisposableImpl
import com.proxy.service.threadpool.info.handler.manager.HandlerController

/**
 * @author: cangHX
 * @data: 2024/7/3 17:25
 * @desc:
 */
open class HandlerLoaderImpl(private val handlerController: HandlerController) : IHandlerLoader {

    protected val delayMillisLocal = ThreadLocal<Long>()
    protected val uptimeMillisLocal = ThreadLocal<Long>()

    override fun start(runnable: Runnable): ITaskDisposable {
        return TaskDisposableImpl(
            handlerController,
            "",
            runnable,
            getDelayMillis(),
            getUptimeMillis()
        )
    }

    override fun start(tag: String, runnable: Runnable): ITaskDisposable {
        return TaskDisposableImpl(
            handlerController,
            tag,
            runnable,
            getDelayMillis(),
            getUptimeMillis()
        )
    }

    private fun getDelayMillis(): Long {
        val delayMillis = delayMillisLocal.get()
        delayMillisLocal.remove()
        return delayMillis ?: 0
    }

    private fun getUptimeMillis(): Long {
        val uptimeMillis = uptimeMillisLocal.get()
        uptimeMillisLocal.remove()
        return uptimeMillis ?: 0
    }
}