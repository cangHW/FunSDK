package com.proxy.service.threadpool.info.handler.controller

import com.proxy.service.threadpool.base.handler.controller.ITaskGroupDisposable
import com.proxy.service.threadpool.info.handler.manager.HandlerController
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/7/3 17:27
 * @desc:
 */
class TaskGroupDisposableImpl(
    private val handlerController: HandlerController?,
    private val runnable: Runnable,
    private val isDisposeTask:AtomicBoolean
) : ITaskGroupDisposable {

    override fun disposeTask() {
        isDisposeTask.set(true)
        handlerController?.getHandler()?.removeCallbacks(runnable)
    }

    override fun disposeGroup() {
        handlerController?.close()
    }

    override fun disposeGroupSafely() {
        handlerController?.closeSafely()
    }

    override fun isTaskDisposed(): Boolean {
        return isDisposeTask.get()
    }

    override fun isGroupDisposed(): Boolean {
        return handlerController?.isCanUse() != true
    }
}