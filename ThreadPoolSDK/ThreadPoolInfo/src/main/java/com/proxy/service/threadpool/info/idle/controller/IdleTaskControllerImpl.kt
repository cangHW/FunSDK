package com.proxy.service.threadpool.info.idle.controller

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.idle.controller.IIdleTaskController
import com.proxy.service.threadpool.info.constants.Constants
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/11/27 20:38
 * @desc:
 */
class IdleTaskControllerImpl(private var runnable: Runnable?) : IIdleTaskController,
    Callable<Boolean> {

    companion object {
        private const val TAG = "${Constants.TAG}_IdleTask"
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
        return runnable == null
    }

    override fun dispose() {
        if (isCompleted.compareAndSet(false, true)) {
            CsLogger.tag(TAG).i("Ready to dispose task.")
            runnable = null
            return
        }
        CsLogger.tag(TAG).i("There is no need to cancel tasks repeatedly.")
    }

    override fun forceRun() {
        if (isCompleted.compareAndSet(false, true)) {
            CsLogger.tag(TAG).i("Prepare to force the current task to run.")
            runnable?.run()
            return
        }
        CsLogger.tag(TAG).i("The current task is running and you do not need to run it again.")
    }

    override fun call(): Boolean {
        if (isCompleted.compareAndSet(false, true)) {
            CsLogger.tag(TAG).i("Ready to run the current task.")
            runnable?.run()
            return true
        }
        return false
    }

}