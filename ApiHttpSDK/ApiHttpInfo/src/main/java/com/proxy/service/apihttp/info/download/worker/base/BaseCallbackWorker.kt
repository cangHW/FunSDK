package com.proxy.service.apihttp.info.download.worker.base

import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.apihttp.info.download.manager.CallbackManager
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/5 17:49
 * @desc:
 */
abstract class BaseCallbackWorker(task: DownloadTask) : BaseStatusWorker(task) {

    private var oldSize: Long = 0
    private var oldTime: Long = 0

    /**
     * 任务开始
     * */
    protected fun callbackStart() {
        if (isCancel()) {
            return
        }
        DownloadRoom.INSTANCE.getTaskDao().updateStatus(
            task.getTaskTag(),
            StatusEnum.START.status
        )
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (isCancel()) {
                    return ""
                }
                CallbackManager.getDownloadCallbacks(task.getTaskTag()).forEach {
                    it.onStart(task)
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

        if (oldSize == 0L || oldTime == 0L) {
            oldSize = currentSize
            oldTime = System.currentTimeMillis()
            return
        }

        val speed = (currentSize - oldSize) * 1000 / (System.currentTimeMillis() - oldTime)
        val progress = if (task.getFileSize() > 0) {
            (Math.round(((currentSize * 100f) / task.getFileSize()) * 100) / 100f).let {
                if (it > 100) {
                    100f
                } else {
                    it
                }
            }
        } else {
            -1f
        }
        DownloadRoom.INSTANCE.getTaskDao().updateStatus(
            task.getTaskTag(),
            StatusEnum.PROGRESS.status
        )
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (isCancel()) {
                    return ""
                }
                CallbackManager.getDownloadCallbacks(task.getTaskTag()).forEach {
                    it.onProgress(task, currentSize, progress, speed)
                }
                return ""
            }
        })?.start()
    }

    /**
     * 任务取消
     * */
    protected fun callbackCancel() {
        DownloadRoom.INSTANCE.getTaskDao().updateStatus(
            task.getTaskTag(),
            StatusEnum.CANCEL.status
        )
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                CallbackManager.getDownloadCallbacks(task.getTaskTag()).forEach {
                    it.onCancel(task)
                }
                CallbackManager.removeTaskAllDownloadCallback(task.getTaskTag())
                return ""
            }
        })?.start()
    }

    /**
     * 任务结束
     * */
    protected fun callbackEnd(isSuccess: Boolean, exception: DownloadException?) {
        if (isCancel()) {
            return
        }
        finishTask()
        if (isSuccess) {
            DownloadRoom.INSTANCE.getTaskDao().updateStatus(
                task.getTaskTag(),
                StatusEnum.SUCCESS.status
            )
        } else {
            DownloadRoom.INSTANCE.getTaskDao().updateStatus(
                task.getTaskTag(),
                StatusEnum.FAILED.status
            )
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (isCancel()) {
                    return ""
                }
                CallbackManager.getDownloadCallbacks(task.getTaskTag()).forEach {
                    if (isSuccess) {
                        it.onSuccess(task)
                    } else {
                        it.onFailed(
                            task,
                            exception ?: DownloadException.createUnknownError("未知异常")
                        )
                    }
                }
                CallbackManager.removeTaskAllDownloadCallback(task.getTaskTag())
                return ""
            }
        })?.start()
    }

}