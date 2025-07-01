package com.proxy.service.apihttp.info.download.worker.base

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/11/5 17:49
 * @desc:
 */
abstract class BaseStatusWorker(val task: DownloadTask) : IWorker {

    protected val tag = "${ApiConstants.LOG_DOWNLOAD_TAG_START}TaskWorker"

    private val isCancel = AtomicBoolean(false)
    private val isFinish = AtomicBoolean(false)

    protected var callback: IWorker.TaskWorkerFinishCallback? = null

    /**
     * 配置任务完成回调
     * */
    override fun setOnFinishedCallback(callback: IWorker.TaskWorkerFinishCallback) {
        this.callback = callback
    }

    /**
     * 获取当前任务绑定的 DownloadTask
     * */
    override fun getDownloadTask(): DownloadTask {
        return task
    }

    /**
     * 取消任务
     * */
    protected open fun cancelTask(taskTag: String): Boolean {
        if (task.getTaskTag() != taskTag) {
            return false
        }
        if (isFinish()) {
            CsLogger.tag(tag).i("该任务已完成, 无法取消.")
            return false
        }
        if (!isCancel.compareAndSet(false, true)) {
            CsLogger.tag(tag).i("该任务已经取消.")
            return false
        }
        finishTask()
        return true
    }

    /**
     * 结束任务
     * */
    protected open fun finishTask() {
        if (isFinish.compareAndSet(false, true)) {
            callback?.onFinished(this, task)
        }
    }

    /**
     * 是否已经 cancel 了
     * */
    protected fun isCancel(): Boolean {
        return isCancel.get()
    }

    /**
     * 是否已经 finish 了
     * */
    protected fun isFinish(): Boolean {
        return isFinish.get()
    }
}