package com.proxy.service.threadpool.info.thread.task.scheduler

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.schedulers.IScheduler
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import com.proxy.service.threadpool.info.thread.task.loader.TaskLoader
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author: cangHX
 * @data: 2024/6/14 10:39
 * @desc:
 */
abstract class TaskScheduler<T : Any>(private var taskInfo: TaskInfo<T>) :
    TaskLoader<T>(taskInfo), ITaskOption<T>, IScheduler<ITaskOption<T>> {
    override fun ioThread(): ITaskOption<T> {
        val scheduler = Schedulers.io()
        taskInfo.setScheduler(scheduler)
        taskInfo.observable = taskInfo.observable?.observeOn(scheduler)
        return this
    }

    override fun computationThread(): ITaskOption<T> {
        val scheduler = Schedulers.computation()
        taskInfo.setScheduler(scheduler)
        taskInfo.observable = taskInfo.observable?.observeOn(scheduler)
        return this
    }

    override fun mainThread(): ITaskOption<T> {
        if (taskInfo.getScheduler() != AndroidSchedulers.mainThread()){
            val scheduler = AndroidSchedulers.mainThread()
            taskInfo.setScheduler(scheduler)
            taskInfo.observable = taskInfo.observable?.observeOn(scheduler)
        }
        return this
    }
}