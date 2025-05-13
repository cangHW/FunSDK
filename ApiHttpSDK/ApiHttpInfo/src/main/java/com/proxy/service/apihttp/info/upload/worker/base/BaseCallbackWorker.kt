package com.proxy.service.apihttp.info.upload.worker.base

import com.proxy.service.apihttp.base.upload.task.UploadTask
import com.proxy.service.apihttp.info.upload.manager.CallbackManager
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/12/19 20:55
 * @desc:
 */
abstract class BaseCallbackWorker(task: UploadTask) : BaseStatusWorker(task) {

    /**
     * 任务开始
     * */
    protected fun callbackStart() {
        if (isCancel()) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (isCancel()) {
                    return ""
                }
                CallbackManager.getUploadCallbacks(getUploadTask().getTaskTag()).forEach {
                    it.onStart(getUploadTask())
                }
                return ""
            }
        })?.start()
    }

    /**
     * 任务进行中
     *
     * @param currentSize   当前已下载大小
     * */
    protected fun callbackProgress(currentSize: Long) {
        if (isCancel()) {
            return
        }

    }

    /**
     * 任务取消
     * */
    protected fun callbackCancel() {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                CallbackManager.getUploadCallbacks(getUploadTask().getTaskTag()).forEach {
                    it.onCancel(getUploadTask())
                }
                CallbackManager.removeTaskAllUploadCallback(getUploadTask().getTaskTag())
                return ""
            }
        })?.start()
    }

    /**
     * 任务结束
     * */
    protected fun callbackEnd(isSuccess: Boolean, throwable: Throwable?) {
        if (isCancel()) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (isCancel()) {
                    return ""
                }
                CallbackManager.getUploadCallbacks(getUploadTask().getTaskTag()).forEach {
                    if (isSuccess) {
                        it.onSuccess(getUploadTask())
                    } else {
                        it.onFailed(getUploadTask(), throwable ?: IllegalArgumentException("未知异常"))
                    }
                }
                CallbackManager.removeTaskAllUploadCallback(getUploadTask().getTaskTag())
                return ""
            }
        })?.start()
    }

}