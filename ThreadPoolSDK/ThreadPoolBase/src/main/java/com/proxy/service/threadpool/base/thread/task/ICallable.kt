package com.proxy.service.threadpool.base.thread.task

/**
 * @author: cangHX
 * @date: 2024/6/14 13:11
 * @desc: 
 */
interface ICallable<T> {
    @Throws(Throwable::class)
    fun accept(): T
}