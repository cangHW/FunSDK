package com.proxy.service.threadpool.info.handler.controller

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.threadpool.base.handler.controller.ITaskDisposable
import com.proxy.service.threadpool.info.constants.Constants
import com.proxy.service.threadpool.info.handler.manager.HandlerController
import com.proxy.service.threadpool.info.handler.manager.TaskInfo
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/7/3 17:27
 * @desc:
 */
class TaskDisposableImpl(
    private val handlerController: HandlerController,
    tag: String,
    private val task: Runnable,
    delayMillis: Long,
    uptimeMillis: Long
) : ITaskDisposable, Runnable {

    companion object {
        private const val TAG = "${Constants.TAG}_HandlerTask"
        private const val STATUS_DEFAULT = 0
        private const val STATUS_COMPLETED = 1
        private const val STATUS_DISPOSED = 0
    }

    private val taskStatus = AtomicInteger(STATUS_DEFAULT)

    init {
        if (handlerController.isCanUse()) {
            try {
                handlerController.startTask(this, TaskInfo.create(tag, this))
                if (delayMillis > 0) {
                    handlerController.getHandler().postDelayed(this, delayMillis)
                } else if (uptimeMillis > 0) {
                    handlerController.getHandler().postAtTime(this, uptimeMillis)
                } else {
                    handlerController.getHandler().post(this)
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
                handlerController.finishTask(this)
            }
        } else {
            CsLogger.tag(TAG).i("The current task cannot be started because the task group is unavailable.")
        }
    }

    override fun isTaskDisposed(): Boolean {
        return taskStatus.get() == STATUS_DISPOSED
    }

    override fun disposeTask() {
        if (taskStatus.compareAndSet(STATUS_DEFAULT, STATUS_DISPOSED)) {
            CsLogger.tag(TAG).i("Ready to dispose the current task.")
            handlerController.finishTask(this)
            handlerController.getHandler().removeCallbacks(this)
            return
        }
        if (taskStatus.get() == STATUS_DISPOSED) {
            CsLogger.tag(TAG)
                .i("The current task has been cancelled. You do not need to dispose it again.")
        } else if (taskStatus.get() == STATUS_COMPLETED) {
            CsLogger.tag(TAG).i("The current task has been completed and cannot be canceled.")
        }
    }

    override fun run() {
        if (taskStatus.compareAndSet(STATUS_DEFAULT, STATUS_COMPLETED)) {
            try {
                handlerController.finishTask(this)
                task.run()
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return
        }
        CsLogger.tag(TAG).d("The current task has been cancelled and cannot run. task: $task")
    }
}