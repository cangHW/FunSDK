package com.proxy.service.threadpool.base.thread.option.base

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import com.proxy.service.threadpool.base.thread.task.IFunction
import com.proxy.service.threadpool.base.thread.task.IPredicate

/**
 * @author: cangHX
 * @data: 2024/6/7 16:41
 * @desc:
 */
interface IAction<T> {

    /**
     * 设置数据变换器
     * */
    fun <R : Any> map(function: IFunction<T, R>): ITaskOption<R>

    /**
     * 设置数据变换器
     * */
    fun <R : Any> flatMap(function: IFunction<T, ITaskOption<R>>): ITaskOption<R>

    /**
     * 设置数据过滤器
     * */
    fun filter(function: IPredicate<T>): ITaskOption<T>

    /**
     * 设置当前任务从头开始重新执行
     *
     * @param count 重新执行次数，默认为无限次
     * */
    fun repeat(count: Long = Long.MAX_VALUE): ITaskOption<T>

}