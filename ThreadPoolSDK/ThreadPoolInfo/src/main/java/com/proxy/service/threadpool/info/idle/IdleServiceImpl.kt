package com.proxy.service.threadpool.info.idle

import com.proxy.service.threadpool.base.idle.IdleService
import com.proxy.service.threadpool.base.idle.controller.IIdleTaskController
import com.proxy.service.threadpool.info.idle.controller.IdleTaskControllerImpl
import com.proxy.service.threadpool.info.idle.manager.IdleHandlerManager

/**
 * @author: cangHX
 * @data: 2024/11/28 09:52
 * @desc:
 */
object IdleServiceImpl : IdleService {
    override fun startWhenIdle(task: Runnable): IIdleTaskController {
        val controller = IdleTaskControllerImpl(task)
        IdleHandlerManager.addTask(controller).let {
            controller.setStatus(it)
        }
        return controller
    }
}