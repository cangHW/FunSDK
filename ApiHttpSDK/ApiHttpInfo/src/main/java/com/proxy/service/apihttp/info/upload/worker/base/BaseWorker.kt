package com.proxy.service.apihttp.info.upload.worker.base

import com.proxy.service.apihttp.base.upload.task.UploadTask
import okhttp3.Call

/**
 * @author: cangHX
 * @data: 2024/12/19 20:09
 * @desc:
 */
abstract class BaseWorker(task: UploadTask) : BaseCallbackWorker(task) {

    companion object {
        const val TASK_RUNNING = "running"
        const val TASK_FINISH = "finish"
    }

    protected var call: Call? = null

    override fun cancelTask(taskTag: String): Boolean {
        val flag = super.cancelTask(taskTag)
        if (flag) {
            call?.cancel()
            call = null
        }
        return flag
    }

    /**
     * 任务是否可以继续执行
     * */
    protected fun isShouldIntercept(runningValue: String): Boolean {
        if (isCancel()) {
            return true
        }
        return runningValue != TASK_RUNNING
    }

}