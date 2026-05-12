package com.proxy.service.threadpool.base.thread.controller

/**
 * @author: cangHX
 * @date: 2024/6/13 18:38
 * @desc: 任务控制器
 */
interface ITaskDisposable {

    /**
     * 取消任务
     * */
    fun dispose()

    /**
     * 判断任务是否取消
     */
    fun isDisposed(): Boolean

}