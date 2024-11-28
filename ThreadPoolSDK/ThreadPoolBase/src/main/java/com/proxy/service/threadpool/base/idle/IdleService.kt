package com.proxy.service.threadpool.base.idle

import com.proxy.service.threadpool.base.idle.controller.IIdleTaskController

/**
 * @author: cangHX
 * @data: 2024/11/27 20:08
 * @desc:
 */
interface IdleService {

    /**
     * 添加一个当且仅当主线程空闲时任务, 执行时间不确定, 运行在主线程, 需要注意耗时与内存泄露
     * */
    fun startWhenIdle(task: Runnable): IIdleTaskController

}