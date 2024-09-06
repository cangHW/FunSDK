package com.proxy.service.threadpool.base.thread

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.schedulers.IScheduler
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/7/3 18:01
 * @desc:
 */
interface ThreadService : IScheduler<ThreadService> {

    /**
     * 创建一个单次执行任务
     * */
    fun <T : Any> call(task: ICallable<T>): ITaskOption<T>

    /**
     * 创建一个多次执行任务
     * */
    fun <T : Any> call(task: IMultiRunnable<T>): ITaskOption<T>

    /**
     * 创建一个周期任务，向下发送任务执行的次数，从 0 开始
     * @param initialDelay  初始延迟时间
     * @param period        循环间隔时间
     * @param unit          时间格式
     * */
    fun interval(initialDelay: Long, period: Long, unit: TimeUnit): ITaskOption<Long>

    /**
     * 设置延迟时间，挂起当前线程
     * @param delay 延迟时间
     * @param unit  时间格式
     * */
    fun delay(delay: Long, unit: TimeUnit): ITaskOption<Long>
}