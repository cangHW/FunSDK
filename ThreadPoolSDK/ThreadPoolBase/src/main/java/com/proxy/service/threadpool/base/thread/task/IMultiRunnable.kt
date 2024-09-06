package com.proxy.service.threadpool.base.thread.task

import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter

/**
 * @author: cangHX
 * @data: 2024/6/14 17:49
 * @desc:
 */
interface IMultiRunnable<T> {

    @Throws(Throwable::class)
    fun accept(emitter: MultiRunnableEmitter<T>)

}