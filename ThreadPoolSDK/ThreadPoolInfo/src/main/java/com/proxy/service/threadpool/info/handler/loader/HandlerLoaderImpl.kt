package com.proxy.service.threadpool.info.handler.loader

import com.proxy.service.threadpool.base.handler.controller.ITaskGroupDisposable
import com.proxy.service.threadpool.base.handler.loader.IHandlerLoader
import com.proxy.service.threadpool.info.handler.controller.TaskGroupDisposableImpl
import com.proxy.service.threadpool.info.handler.info.HandlerInfo
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/7/3 17:25
 * @desc:
 */
open class HandlerLoaderImpl(private val handlerInfo: HandlerInfo) : IHandlerLoader {
    override fun start(runnable: Runnable): ITaskGroupDisposable {
        val isDisposeTask = AtomicBoolean(false)
        val task = Runnable {
            try {
                runnable.run()
            } finally {
                isDisposeTask.set(true)
            }
        }
        val disposable = TaskGroupDisposableImpl(handlerInfo.handlerController, task, isDisposeTask)
        handlerInfo.doRun(task)
        return disposable
    }
}