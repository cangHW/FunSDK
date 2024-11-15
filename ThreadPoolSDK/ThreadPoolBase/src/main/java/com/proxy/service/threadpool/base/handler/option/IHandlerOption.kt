package com.proxy.service.threadpool.base.handler.option

import com.proxy.service.threadpool.base.handler.loader.IHandlerLoader
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/7/3 17:00
 * @desc:
 */
interface IHandlerOption : IHandlerLoader {

    /**
     * 设置任务延迟时间
     * */
    fun setDelay(timeout: Long, unit: TimeUnit): IHandlerLoader

    /**
     * 设置任务在对应时间点执行, 依赖于 SystemClock.uptimeMillis() 计算时间
     * */
    fun setAtTime(uptimeMillis: Long): IHandlerLoader

    /**
     * 获取当前线程 id, 可用于校验任务是否运行在该线程中
     * */
    fun getThreadId(): Long
}