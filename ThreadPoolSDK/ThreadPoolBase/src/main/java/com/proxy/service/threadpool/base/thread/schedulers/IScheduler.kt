package com.proxy.service.threadpool.base.thread.schedulers

/**
 * @author: cangHX
 * @data: 2024/6/7 15:30
 * @desc:
 */
interface IScheduler<T> {

    /**
     * 切换到 io 线程
     * */
    fun ioThread(): T

    /**
     * 切换到 CPU 计算型线程, 不建议在线程内部使用阻塞等操作，如果线程长时间处于挂起状态，可能会被销毁
     * */
    fun computationThread(): T

    /**
     * 切换到主线程
     * */
    fun mainThread(): T

}