package com.proxy.service.threadpool.base.handler.controller

/**
 * @author: cangHX
 * @data: 2024/12/13 10:31
 * @desc: 组任务控制器
 */
interface IGroupController {

    /**
     * 判断任务组是否取消
     * */
    fun isGroupDisposed(): Boolean

    /**
     * 移除组内全部还未运行的任务
     * */
    fun clearAllTask()

    /**
     * 移除组内全部还未运行的任务
     * */
    fun clearAllTaskWithTag(tag: String)

    /**
     * 取消当前任务组, 立刻取消，放弃组内任务
     * */
    fun disposeGroup()

    /**
     * 安全的取消当前任务组, 等待组内任务全部执行完后自动取消
     * */
    fun disposeGroupSafely()

}