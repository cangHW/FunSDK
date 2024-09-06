package com.proxy.service.threadpool.info.thread.task

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.option.base.IObserver
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.info.thread.info.TaskInfo
import com.proxy.service.threadpool.info.thread.task.option.TaskOption
import io.reactivex.Observable

/**
 * @author: cangHX
 * @data: 2024/6/7 17:22
 * @desc:
 */
class TaskOptionImpl<T : Any>(private var taskInfo: TaskInfo<T>) :
    TaskOption<T>(taskInfo), IObserver<T>, IRealObserver<T> {

    override fun getObserver(): Observable<T>? {
        return taskInfo.observable
    }

    override fun doOnNext(task: IConsumer<T>): ITaskOption<T> {
        taskInfo.observable = taskInfo.observable?.doOnNext {
            task.accept(it)
        }
        return this
    }

}