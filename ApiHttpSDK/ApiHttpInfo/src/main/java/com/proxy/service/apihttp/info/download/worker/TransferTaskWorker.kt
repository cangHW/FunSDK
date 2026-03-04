package com.proxy.service.apihttp.info.download.worker

import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.apihttp.info.download.manager.CallbackManager
import com.proxy.service.apihttp.info.download.worker.base.IWorker
import com.proxy.service.apihttp.info.download.worker.task.Task
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2026/3/4 09:59
 * @desc:
 */
class TransferTaskWorker(private val task: DownloadTask) : IWorker {

    private val tag = "${ApiConstants.LOG_DOWNLOAD_TAG_START}TransferTask"

    @Volatile
    private var worker: IWorker? = null
    private var callback: IWorker.TaskWorkerFinishCallback? = null

    private val lock = Any()
    private val isCancel = AtomicBoolean(false)

    override fun setOnFinishedCallback(callback: IWorker.TaskWorkerFinishCallback) {
        this.callback = callback
    }

    override fun startTask() {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                //1.检查文件大小
                if (task.getFileSize() != ApiConstants.Download.TOTAL_FILE_SIZE) {
                    return ""
                }
                //2.获取远程文件大小
                try {
                    val size = Task.getRemoteFileSize(task)
                    if (size != null) {
                        val builder = task.builder()
                        if (builder is DownloadTask.Builder) {
                            builder.setFileSize(size)
                            builder.checkMultiPartEnable()
                        }
                        DownloadRoom.INSTANCE.getTaskDao().updateDownloadTask(task)
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag)
                        .w(throwable, "获取远端文件大小失败. taskTag = ${task.getTaskTag()}")
                }
                return ""
            }
        })?.doOnNext(object : IConsumer<String> {
            override fun accept(value: String) {
                synchronized(lock) {
                    if (isCancel.get()) {
                        return
                    }

                    worker = if (task.getMultiPartEnable()) {
                        MultiTaskWorker(task)
                    } else {
                        SingleTaskWorker(task)
                    }
                    callback?.let {
                        worker?.setOnFinishedCallback(it)
                    }
                    worker?.startTask()
                }
            }
        })?.start()
    }

    override fun cancelTask(taskTag: String, isNeedCallback: Boolean) {
        if (task.getTaskTag() != taskTag) {
            return
        }
        synchronized(lock) {
            isCancel.set(true)

            val worker = this.worker
            if (worker != null) {
                worker.cancelTask(taskTag, isNeedCallback)
                return
            }

            CsLogger.tag(tag).i("取消任务. taskTag = ${task.getTaskTag()}")
            if (isNeedCallback) {
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
        }
    }

    override fun getDownloadTask(): DownloadTask {
        return task
    }

}