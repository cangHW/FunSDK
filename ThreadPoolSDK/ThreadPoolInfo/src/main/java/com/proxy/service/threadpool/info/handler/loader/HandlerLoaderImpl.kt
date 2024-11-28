package com.proxy.service.threadpool.info.handler.loader

import com.proxy.service.threadpool.base.handler.controller.ITaskGroupDisposable
import com.proxy.service.threadpool.base.handler.loader.IHandlerLoader
import com.proxy.service.threadpool.info.handler.controller.TaskGroupDisposableImpl
import com.proxy.service.threadpool.info.handler.info.HandlerInfo

/**
 * @author: cangHX
 * @data: 2024/7/3 17:25
 * @desc:
 */
open class HandlerLoaderImpl(private val handlerInfo: HandlerInfo) : IHandlerLoader {
    override fun start(runnable: Runnable): ITaskGroupDisposable {
        return TaskGroupDisposableImpl(handlerInfo, runnable)
    }
}