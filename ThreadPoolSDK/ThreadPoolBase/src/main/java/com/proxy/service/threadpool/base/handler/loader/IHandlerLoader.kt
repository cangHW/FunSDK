package com.proxy.service.threadpool.base.handler.loader

import com.proxy.service.threadpool.base.handler.controller.ITaskDisposable

/**
 * @author: cangHX
 * @data: 2024/7/3 16:59
 * @desc:
 */
interface IHandlerLoader {

    /**
     * 开始任务
     * */
    fun start(runnable: Runnable): ITaskDisposable

}