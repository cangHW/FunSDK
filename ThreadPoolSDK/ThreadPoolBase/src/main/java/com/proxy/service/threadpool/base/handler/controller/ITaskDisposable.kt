package com.proxy.service.threadpool.base.handler.controller

/**
 * @author: cangHX
 * @data: 2024/6/13 18:38
 * @desc: 任务控制器
 */
interface ITaskDisposable {

    /**
     * 判断当前任务是否取消
     */
    fun isTaskDisposed(): Boolean

    /**
     * 取消当前任务，如果当前任务还未执行
     * */
    fun disposeTask()

}