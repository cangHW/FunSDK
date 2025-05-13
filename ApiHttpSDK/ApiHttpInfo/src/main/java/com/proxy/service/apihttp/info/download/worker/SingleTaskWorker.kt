package com.proxy.service.apihttp.info.download.worker

import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.utils.FileUtils
import com.proxy.service.apihttp.info.download.worker.base.BaseWorker
import com.proxy.service.apihttp.info.download.worker.task.Task
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.OnCompleteCallback
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IFunction
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/10/31 11:32
 * @desc:
 */
class SingleTaskWorker(task: DownloadTask) : BaseWorker(task) {

    private val msg = BaseTaskMsg()

    init {
        addTaskMsg(msg)
    }

    /**
     * 进度监听控制器
     * */
    private var disposable: ITaskDisposable? = null

    /**
     * 开始任务
     * */
    override fun onStartTask() {
        CsLogger.tag(tag).i("开始执行普通任务. taskTag = ${task.getTaskTag()}")
        msg.filePath = FileUtils.getTempPath(task.getFilePath())

        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                //1.任务开始
                callbackStart()
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //2.任务预处理, 用于校验当前文件是否已存在, 并校验是否满足要求
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag).i("任务预处理. taskTag = ${task.getTaskTag()}")
                if (preTaskCheck()) {
                    return TASK_FINISH
                }
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //3.创建临时文件, 准备下载
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag).i("创建临时文件. taskTag = ${task.getTaskTag()}")
                if (!CsFileUtils.createFile(msg.filePath)) {
                    CsLogger.tag(tag).i("创建文件失败. taskTag = ${task.getTaskTag()}")
                    callbackEnd(
                        false,
                        DownloadException.create(
                            DownloadException.CREATE_FILE_FAILURE,
                            "创建文件失败"
                        )
                    )
                    return TASK_FINISH
                }
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //4.创建进度监控器
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag).i("创建进度监控器. taskTag = ${task.getTaskTag()}")
                disposable = checkProgress()
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //5.开始下载
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag).i("开始下载. taskTag = ${task.getTaskTag()}")
                try {
                    Task.start(task, msg.filePath) {
                        //得到下载流，用于取消下载
                        if (isCancel()) {
                            CsFileUtils.close(it)
                        } else {
                            msg.stream = it
                        }
                    }
                    return TASK_RUNNING
                } catch (throwable: Throwable) {
                    //下载失败
                    if (throwable is DownloadException) {
                        callbackEnd(false, throwable)
                    } else {
                        callbackEnd(
                            false,
                            DownloadException.createUnknownError(throwable.message ?: "未知异常")
                        )
                    }
                    return TASK_FINISH
                }
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //6.检查文件
                if (isShouldIntercept(value)) {
                    return value
                }
                try {
                    endTaskCheck(msg.filePath)
                    return TASK_RUNNING
                } catch (throwable: Throwable) {
                    //文件有问题
                    if (throwable is DownloadException) {
                        callbackEnd(false, throwable)
                    } else {
                        callbackEnd(
                            false,
                            DownloadException.createUnknownError(throwable.message ?: "未知异常")
                        )
                    }
                    return TASK_FINISH
                }
            }
        })?.doOnNext(object : IConsumer<String> {
            override fun accept(value: String) {
                //7.重命名文件
                if (isShouldIntercept(value)) {
                    return
                }
                CsLogger.tag(tag).i("开始重命名文件. path = ${msg.filePath}")
                if (!CsFileUtils.rename(msg.filePath, task.getFilePath())) {
                    CsLogger.tag(tag)
                        .i("文件重命名失败. srcPath = ${msg.filePath}, destPath = ${task.getFilePath()}")
                    callbackEnd(
                        false,
                        DownloadException.create(
                            DownloadException.FILE_RENAME_FAILURE,
                            "文件重命名失败"
                        )
                    )
                    return
                }
                CsLogger.tag(tag).i("重命名文件成功. path = ${task.getFilePath()}")
                //下载成功
                callbackEnd(true, null)
            }
        })?.setOnCompleteCallback(object : OnCompleteCallback {
            override fun onCallback() {
                //8.下载结束
                disposable?.dispose()
                CsLogger.tag(tag).i("下载结束. taskTag = ${task.getTaskTag()}")
            }
        })?.start()
    }

    /**
     * 垃圾回收
     * */
    override fun onClear() {

    }

    /**
     * 检查下载进度
     * */
    private fun checkProgress(): ITaskDisposable? {
        var disposable: ITaskDisposable? = null
        disposable = CsTask.computationThread()?.delay(1, TimeUnit.SECONDS)
            ?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    if (checkFile(msg.filePath)) {
                        if (disposable?.isDisposed() != true) {
                            disposable?.dispose()
                        }
                        callbackEnd(
                            false,
                            DownloadException.create(
                                DownloadException.FILE_NOT_FOUND,
                                "未知异常, 文件丢失. taskTag = ${task.getTaskTag()}"
                            )
                        )
                        return
                    }

                    if (msg.stream == null) {
                        return
                    }

                    val tempLength = CsFileUtils.length(msg.filePath)
                    if (tempLength > 0) {
                        callbackProgress(tempLength)
                    }
                }
            })?.repeat()?.start()
        return disposable
    }

}