package com.proxy.service.threadpool.base.thread.task

/**
 * @author: cangHX
 * @date: 2024/6/14 13:08
 * @desc:
 */
interface IConsumer<T> {
    @Throws(Throwable::class)
    fun accept(value: T)
}