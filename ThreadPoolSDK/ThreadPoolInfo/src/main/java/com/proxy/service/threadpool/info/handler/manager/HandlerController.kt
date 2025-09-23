package com.proxy.service.threadpool.info.handler.manager

import android.os.Handler

/**
 * @author: cangHX
 * @data: 2024/7/3 17:48
 * @desc:
 */
interface HandlerController {

    /**
     * 获取线程名称
     * */
    fun getThreadName(): String

    /**
     * 获取线程 ID
     * */
    fun getThreadId(): Long

    /**
     * 获取真实 handler 对象
     * */
    fun getHandler(): Handler

    /**
     * 开始一个任务
     * */
    fun startTask(key: Any, value: TaskInfo)

    /**
     * 结束一个任务
     * */
    fun finishTask(key: Any)

    /**
     * 取消待运行任务
     * */
    fun cancelTaskByTag(tag: String)

    /**
     * 取消待运行任务
     * */
    fun cancelAllTask()

    /**
     * 当前 handler 是否可用
     * */
    fun isCanUse(): Boolean

    /**
     * 立刻关闭
     * */
    fun close()

    /**
     * 安全关闭
     * */
    fun closeSafely()

}