package com.proxy.service.threadpool.info.idle.controller

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.idle.controller.IIdleTaskController
import com.proxy.service.threadpool.base.constants.ThreadConstants
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/11/27 20:38
 * @desc:
 */
class IdleTaskControllerImpl(
    private var task: Runnable?
) : IIdleTaskController, Callable<Boolean> {

    companion object {
        private const val TAG = "${ThreadConstants.TAG}_IdleTask"
    }

    private val isCompleted = AtomicBoolean(false)
    private var isAddSuccess: Boolean = true

    fun setStatus(isAddSuccess: Boolean) {
        this.isAddSuccess = isAddSuccess
    }

    override fun isAdded(): Boolean {
        return isAddSuccess
    }

    override fun isCompleted(): Boolean {
        return isCompleted.get()
    }

    override fun isDisposed(): Boolean {
        return task == null
    }

    override fun dispose() {
        if (isCompleted.compareAndSet(false, true)) {
            CsLogger.tag(TAG).i("Ready to dispose idle task.")
            task = null
            return
        }
        CsLogger.tag(TAG).i("There is no need to cancel idle tasks repeatedly.")
    }

    override fun forceRun() {
        if (isCompleted.compareAndSet(false, true)) {
            CsLogger.tag(TAG).i("Prepare to force the current idle task to run.")
            try {
                task?.run()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return
        }
        CsLogger.tag(TAG).i("The current idle task is running and you do not need to run it again.")
    }

    override fun call(): Boolean {
        if (isCompleted.compareAndSet(false, true)) {
            CsLogger.tag(TAG).d("Ready to run the current idle task.")
            try {
                task?.run()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return true
        }
        return false
    }

}