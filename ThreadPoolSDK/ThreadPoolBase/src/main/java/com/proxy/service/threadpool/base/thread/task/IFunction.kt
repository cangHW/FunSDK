package com.proxy.service.threadpool.base.thread.task

/**
 * @author: cangHX
 * @date: 2024/6/14 11:52
 * @desc:
 */
interface IFunction<T, R> {

    @Throws(Throwable::class)
    fun apply(value: T): R

}