package com.proxy.service.threadpool.info.thread.task.action

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.option.base.IAction
import com.proxy.service.threadpool.base.thread.task.IFunction
import com.proxy.service.threadpool.base.thread.task.IPredicate
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import com.proxy.service.threadpool.info.thread.task.IRealObserver
import com.proxy.service.threadpool.info.thread.task.TaskOptionImpl
import com.proxy.service.threadpool.info.thread.task.scheduler.TaskScheduler

/**
 * @author: cangHX
 * @data: 2024/6/14 13:00
 * @desc:
 */
abstract class TaskAction<T : Any>(
    private var taskInfo: TaskInfo<T>
) : TaskScheduler<T>(taskInfo), IAction<T> {

    override fun <R : Any> map(function: IFunction<T, R>): ITaskOption<R> {
        val task = TaskInfo<R>()
        task.copy(taskInfo)
        task.observable = taskInfo.observable?.map { t -> function.apply(t) }
        return TaskOptionImpl(task)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <R : Any> flatMap(function: IFunction<T, ITaskOption<R>>): ITaskOption<R> {
        val task = TaskInfo<R>()
        task.copy(taskInfo)
        task.observable = taskInfo.observable?.flatMap {
            val iTaskOption = function.apply(it)
            if (iTaskOption is IRealObserver<*>){
                val observer = iTaskOption as IRealObserver<R>
                observer.getObserver()
            }else{
                throw IllegalArgumentException("Invalid function : $function, it must be created using ThreadPoolService.")
            }
        }
        return TaskOptionImpl(task)
    }

    override fun filter(function: IPredicate<T>): ITaskOption<T> {
        taskInfo.observable = taskInfo.observable?.filter { t -> function.check(t) }
        return this
    }

    override fun repeat(count: Long): ITaskOption<T> {
        taskInfo.observable = taskInfo.observable?.repeat(count)
        return this
    }

}