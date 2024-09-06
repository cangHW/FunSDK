package com.proxy.service.threadpool.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.threadpool.base.ThreadPoolService
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import com.proxy.service.threadpool.base.thread.ThreadService
import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import com.proxy.service.threadpool.info.handler.HandlerServiceImpl
import com.proxy.service.threadpool.info.thread.ThreadServiceImpl
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/27 16:43
 * @desc:
 */
@CloudApiService(serviceTag = "service/thread_pool")
class ThreadPoolServiceImpl : ThreadPoolService {

    override fun <T : Any> call(task: ICallable<T>): ITaskOption<T> {
        val threadService = ThreadServiceImpl()
        return threadService.call(task)
    }

    override fun <T : Any> call(task: IMultiRunnable<T>): ITaskOption<T> {
        val threadService = ThreadServiceImpl()
        return threadService.call(task)
    }

    override fun interval(initialDelay: Long, period: Long, unit: TimeUnit): ITaskOption<Long> {
        val threadService = ThreadServiceImpl()
        return threadService.interval(initialDelay, period, unit)
    }

    override fun delay(delay: Long, unit: TimeUnit): ITaskOption<Long> {
        val threadService = ThreadServiceImpl()
        return threadService.delay(delay, unit)
    }

    override fun ioThread(): ThreadService {
        val threadService = ThreadServiceImpl()
        return threadService.ioThread()
    }

    override fun computationThread(): ThreadService {
        val threadService = ThreadServiceImpl()
        return threadService.computationThread()
    }

    override fun mainThread(): ThreadService {
        val threadService = ThreadServiceImpl()
        return threadService.mainThread()
    }

    override fun launchTaskGroup(groupName: String): IHandlerOption {
        val handlerService = HandlerServiceImpl()
        return handlerService.launchTaskGroup(groupName)
    }

}