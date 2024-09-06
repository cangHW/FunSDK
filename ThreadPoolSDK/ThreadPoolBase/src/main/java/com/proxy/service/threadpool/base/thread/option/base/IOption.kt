package com.proxy.service.threadpool.base.thread.option.base

import com.proxy.service.threadpool.base.thread.option.ITaskOption
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/6/7 16:41
 * @desc:
 */
interface IOption<T> {

    /**
     * 设置超时时间，挂起当前线程
     * @param timeout   超时时间
     * @param unit      时间格式
     * */
    fun timeout(timeout: Long, unit: TimeUnit): ITaskOption<T>

    /**
     * 设置延迟时间，挂起当前线程
     * @param delay 延迟时间
     * @param unit  时间格式
     * */
    fun delay(delay: Long, unit: TimeUnit): ITaskOption<T>

}