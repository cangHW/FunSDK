package com.proxy.service.threadpool.base.thread.option.base

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.task.IConsumer

/**
 * @author: cangHX
 * @data: 2024/6/14 14:44
 * @desc:
 */
interface IObserver<T> {

    /**
     * 设置下一步执行任务
     * */
    fun doOnNext(task: IConsumer<T>): ITaskOption<T>
}