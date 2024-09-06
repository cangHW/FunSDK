package com.proxy.service.core.service.task

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.threadpool.base.ThreadPoolService
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import com.proxy.service.threadpool.base.thread.ThreadService
import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/6/17 15:08
 * @desc: 线程调度框架入口
 */
object CsTask {

    private var service: ThreadPoolService? = null

    private fun getService(): ThreadPoolService? {
        if (service == null) {
            service = CloudSystem.getService(ThreadPoolService::class.java)
        }
        if (service == null) {
            CsLogger.e("Please check to see if it is referenced. <com.tal.sc.reader:service-threadpool:xxx>")
        }
        return service
    }

    /**
     * 创建一个单次执行任务
     * */
    fun <T : Any> call(task: ICallable<T>): ITaskOption<T>? {
        return getService()?.call(task)
    }

    /**
     * 创建一个多次执行任务
     * */
    fun <T : Any> call(task: IMultiRunnable<T>): ITaskOption<T>? {
        return getService()?.call(task)
    }

    /**
     * 创建一个循环任务，向下发送任务执行的次数，从 0 开始
     * @param initialDelay  初始延迟时间
     * @param period        循环间隔时间
     * @param unit          时间格式
     * */
    fun interval(initialDelay: Long, period: Long, unit: TimeUnit): ITaskOption<Long>?{
        return getService()?.interval(initialDelay, period, unit)
    }

    /**
     * 设置延迟时间，如果未显示修改任务线程，默认使用子线程，避免阻塞主线程
     * */
    fun delay(timeout: Long, unit: TimeUnit): ITaskOption<Long>? {
        return getService()?.delay(timeout, unit)
    }

    /**
     * 切换到 CPU 计算型线程
     * */
    fun computationThread(): ThreadService? {
        return getService()?.computationThread()
    }

    /**
     * 切换到 io 线程
     * */
    fun ioThread(): ThreadService? {
        return getService()?.ioThread()
    }

    /**
     * 切换到主线程
     * */
    fun mainThread(): ThreadService? {
        return getService()?.mainThread()
    }

    /**
     * 启动一个子线程任务组，如果当前组存在, 则使用，如果不存在则创建, 同一个组内任务顺序执行
     * */
    fun launchTaskGroup(groupName: String): IHandlerOption? {
        return getService()?.launchTaskGroup(groupName)
    }

}