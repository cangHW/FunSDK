package com.proxy.service.threadpool.base.handler.controller

/**
 * @author: cangHX
 * @data: 2024/6/13 18:38
 * @desc: 组任务控制器
 */
interface ITaskGroupDisposable {

    /**
     * 取消当前任务，如果当前任务还未执行
     * */
    fun disposeTask()

    /**
     * 取消当前任务组, 立刻取消，放弃组内任务
     * */
    fun disposeGroup()

    /**
     * 安全的取消当前任务组, 等待组内任务全部执行完后自动取消
     * */
    fun disposeGroupSafely()

    /**
     * 判断当前任务是否取消
     */
    fun isTaskDisposed(): Boolean

    /**
     * 判断任务组是否取消
     * */
    fun isGroupDisposed(): Boolean
}