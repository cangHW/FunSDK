package com.proxy.service.threadpool.info.handler.option

import com.proxy.service.threadpool.base.handler.loader.IHandlerLoader
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import com.proxy.service.threadpool.info.handler.info.HandlerInfo
import com.proxy.service.threadpool.info.handler.loader.HandlerLoaderImpl
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/7/3 17:27
 * @desc:
 */
class HandlerOptionImpl(private val handlerInfo: HandlerInfo) :
    HandlerLoaderImpl(handlerInfo), IHandlerOption {
    override fun setDelay(timeout: Long, unit: TimeUnit): IHandlerLoader {
        handlerInfo.delay = unit.toMillis(timeout)
        return this
    }

    override fun setAtTime(uptimeMillis: Long): IHandlerLoader {
        handlerInfo.uptimeMillis = uptimeMillis
        return this
    }
}