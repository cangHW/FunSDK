package com.proxy.service.threadpool.info.thread.task.option

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.option.base.IOption
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import com.proxy.service.threadpool.info.thread.task.action.TaskAction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/6/14 15:10
 * @desc:
 */
abstract class TaskOption<T : Any>(
    private var taskInfo: TaskInfo<T>
) : TaskAction<T>(taskInfo), IOption<T> {

    override fun timeout(timeout: Long, unit: TimeUnit): ITaskOption<T> {
        taskInfo.observable = taskInfo.observable?.timeout(timeout, unit, Schedulers.computation())
        taskInfo.getScheduler()?.let {
            taskInfo.observable = taskInfo.observable?.observeOn(it)
        } ?: let {
            taskInfo.setScheduler(Schedulers.computation())
        }
        return this
    }

    override fun delay(delay: Long, unit: TimeUnit): ITaskOption<T> {
        taskInfo.observable = taskInfo.observable?.delay(delay, unit, Schedulers.computation())
        taskInfo.getScheduler()?.let {
            taskInfo.observable = taskInfo.observable?.observeOn(it)
        } ?: let {
            taskInfo.setScheduler(Schedulers.computation())
        }
        return this
    }

}