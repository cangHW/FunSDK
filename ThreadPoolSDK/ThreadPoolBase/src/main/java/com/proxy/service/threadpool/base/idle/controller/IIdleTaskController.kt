package com.proxy.service.threadpool.base.idle.controller

/**
 * @author: cangHX
 * @data: 2024/11/27 20:12
 * @desc:
 */
interface IIdleTaskController {

    /**
     * 是否添加成功
     * */
    fun isAdded(): Boolean

    /**
     * 是否结束, 不代表执行完成, 需要综合[isAdded]、[isDisposed]、[isCompleted]判断
     * */
    fun isCompleted(): Boolean

    /**
     * 判断任务是否取消
     */
    fun isDisposed(): Boolean

    /**
     * 取消任务
     * */
    fun dispose()

    /**
     * 强制执行, 不再等待主线程空闲
     * */
    fun forceRun()

}