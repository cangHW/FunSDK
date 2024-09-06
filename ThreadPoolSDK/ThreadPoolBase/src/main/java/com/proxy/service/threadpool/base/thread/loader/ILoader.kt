package com.proxy.service.threadpool.base.thread.loader

import com.proxy.service.threadpool.base.thread.callback.OnSuccessCallback
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.callback.OnRunCallback
import com.proxy.service.threadpool.base.thread.callback.OnStartCallback
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable

/**
 * @author: cangHX
 * @data: 2024/6/13 11:41
 * @desc:
 */
interface ILoader<T> {

    /**
     * 设置任务开始回调
     * */
    fun setOnStartCallback(callback: OnStartCallback): ILoader<T>

    /**
     * 设置任务完成回调
     * */
    fun setOnRunCallback(callback: OnRunCallback<T>): ILoader<T>

    /**
     * 设置任务出错回调
     * */
    fun setOnFailedCallback(callback: OnFailedCallback): ILoader<T>

    /**
     * 设置任务结束回调
     * */
    fun setOnSuccessCallback(callback: OnSuccessCallback): ILoader<T>

    /**
     * 开始任务
     * */
    fun start(): ITaskDisposable

    /**
     * 阻塞，直到获取到最终结果
     * */
    @Throws(Throwable::class)
    fun blockGetFirst(): T?

    /**
     * 阻塞，直到获取到最终结果
     * */
    @Throws(Throwable::class)
    fun blockGetLast(): T?
}