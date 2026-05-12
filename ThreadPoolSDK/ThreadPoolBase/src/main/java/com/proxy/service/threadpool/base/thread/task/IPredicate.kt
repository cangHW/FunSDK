package com.proxy.service.threadpool.base.thread.task

/**
 * @author: cangHX
 * @date: 2024/6/14 16:17
 * @desc:
 */
interface IPredicate<T> {

    @Throws(Throwable::class)
    fun check(value: T): Boolean

}