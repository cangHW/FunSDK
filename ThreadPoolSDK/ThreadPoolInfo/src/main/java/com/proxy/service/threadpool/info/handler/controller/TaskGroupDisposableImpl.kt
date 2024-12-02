package com.proxy.service.threadpool.info.handler.controller

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.handler.controller.ITaskGroupDisposable
import com.proxy.service.threadpool.info.constants.Constants
import com.proxy.service.threadpool.info.handler.info.HandlerInfo
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/7/3 17:27
 * @desc:
 */
class TaskGroupDisposableImpl(
    private val handler: HandlerInfo,
    private val task: Runnable
) : ITaskGroupDisposable, Runnable {

    companion object {
        private const val TAG = "${Constants.TAG}_HandlerTask"
        private const val STATUS_DEFAULT = 0
        private const val STATUS_COMPLETED = 1
        private const val STATUS_DISPOSED = 0
    }

    private val taskStatus = AtomicInteger(STATUS_DEFAULT)

    init {
        try {
            if (handler.delay > 0) {
                handler.handlerController.getHandler().postDelayed(this, handler.delay)
            } else if (handler.uptimeMillis > 0) {
                handler.handlerController.getHandler().postAtTime(this, handler.uptimeMillis)
            } else {
                handler.handlerController.getHandler().post(this)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    override fun isTaskDisposed(): Boolean {
        return taskStatus.get() == STATUS_DISPOSED
    }

    override fun disposeTask() {
        if (taskStatus.compareAndSet(STATUS_DEFAULT, STATUS_DISPOSED)) {
            CsLogger.tag(TAG).i("Ready to dispose the current task.")
            handler.handlerController.getHandler().removeCallbacks(this)
            return
        }
        if (taskStatus.get() == STATUS_DISPOSED) {
            CsLogger.tag(TAG)
                .i("The current task has been cancelled. You do not need to dispose it again.")
        } else if (taskStatus.get() == STATUS_COMPLETED) {
            CsLogger.tag(TAG).i("The current task has been completed and cannot be canceled.")
        }
    }

    override fun isGroupDisposed(): Boolean {
        return !handler.handlerController.isCanUse()
    }

    override fun disposeGroup() {
        handler.handlerController.close()
    }

    override fun disposeGroupSafely() {
        handler.handlerController.closeSafely()
    }

    override fun run() {
        if (taskStatus.compareAndSet(STATUS_DEFAULT, STATUS_COMPLETED)) {
            CsLogger.tag(TAG).i("The current task is ready to run. task: $task")
            try {
                task.run()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return
        }
        CsLogger.tag(TAG).i("The current task has been cancelled and cannot run. task: $task")
    }
}