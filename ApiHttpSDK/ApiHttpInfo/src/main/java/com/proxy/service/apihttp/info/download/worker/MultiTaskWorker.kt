package com.proxy.service.apihttp.info.download.worker

import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.constants.ApiConstants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.utils.FileUtils
import com.proxy.service.apihttp.info.download.worker.base.BaseWorker
import com.proxy.service.apihttp.info.download.worker.task.Task
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.OnCompleteCallback
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IFunction
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/10/31 11:32
 * @desc:
 */
class MultiTaskWorker(task: DownloadTask) : BaseWorker(task) {

    companion object {
        private const val MAX_PART_RUN = 3

        private const val STATUS_NORMAL = "status_normal"
        private const val STATUS_START = "status_start"
        private const val STATUS_SUCCESS = "status_success"
        private const val STATUS_FAILED = "status_failed"
    }

    private class MultiMsg : BaseTaskMsg() {

        //下载任务区间开始位置
        var partStartIndex: Long = 0L

        //下载任务区间结束位置, -1 代表到文件结尾
        var partSize: Long = -1L

        //分片位置信息
        var partPosition = 0

        //分片状态
        var status = STATUS_NORMAL
    }

    private var latch: CountDownLatch? = null

    private val partDir = FileUtils.getPartDir(task)
    private var tempPath = FileUtils.getTempPath(task.getFilePath())

    init {
        var start = 0L
        var index = 0
        while (start < task.getFileSize()) {
            val msg = MultiMsg()
            msg.partPosition = index
            msg.filePath = "$partDir${File.separator}${task.getTaskTag()}.part${index}"
            msg.partStartIndex = start
            msg.partSize = (task.getFileSize() - start).let {
                if (it > ApiConstants.Download.FILE_PART_SIZE) {
                    ApiConstants.Download.FILE_PART_SIZE
                } else {
                    it
                }
            }
            addTaskMsg(msg)

            start += ApiConstants.Download.FILE_PART_SIZE
            index++
        }

        latch = CountDownLatch(msgMaxCache.size())
    }

    /**
     * 进度监听控制器
     * */
    private var disposable: ITaskDisposable? = null

    /**
     * 开始任务
     * */
    override fun onStartTask() {
        CsLogger.tag(tag).i("开始执行分片任务. taskTag = ${task.getTaskTag()}")

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
                return value
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //3.创建进度监控器
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag).i("创建进度监控器. taskTag = ${task.getTaskTag()}")
                disposable = checkProgress()
                return value
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //4.开始下载
                if (isShouldIntercept(value)) {
                    return value
                }
                tryStartTask()
                return value
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //5.等待分片任务结束
                if (isShouldIntercept(value)) {
                    return value
                }
                try {
                    latch?.await()
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).d(throwable)
                }
                disposable?.dispose()
                return value
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //6.检查分片任务
                if (isShouldIntercept(value)) {
                    return value
                }
                msgMaxCache.getAllCache().forEach {
                    if (it is MultiMsg) {
                        if (it.status != STATUS_SUCCESS) {
                            CsLogger.tag(tag).i("下载失败. taskTag = ${task.getTaskTag()}")
                            callbackEnd(
                                false,
                                DownloadException.create(
                                    DownloadException.MULTI_TASK_HAS_FAILURE,
                                    "下载失败"
                                )
                            )
                            return TASK_FINISH
                        }
                    }
                }
                return value
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //7.合并分片
                if (isShouldIntercept(value)) {
                    return value
                }
                if (!CsFileUtils.createFile(tempPath)) {
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
                msgMaxCache.getAllCache().forEach {
                    if (it is MultiMsg) {
                        CsLogger.tag(tag)
                            .i("开始合并分片. taskTag = ${task.getTaskTag()}, part: ${it.partPosition}, totalPart: ${msgMaxCache.size()}")
                        if (
                            !CsFileWriteUtils.setSourcePath(it.filePath).writeSync(tempPath, true)
                        ) {
                            CsFileUtils.delete(tempPath)
                            CsLogger.tag(tag)
                                .i("合并分片失败. taskTag = ${task.getTaskTag()}, part: ${it.partPosition}, totalPart: ${msgMaxCache.size()}")
                            callbackEnd(
                                false,
                                DownloadException.create(
                                    DownloadException.FILE_MERGE_FAILURE,
                                    "合并文件失败"
                                )
                            )
                            return TASK_FINISH
                        }
                        CsLogger.tag(tag)
                            .i("分片合并成功. taskTag = ${task.getTaskTag()}, part: ${it.partPosition}, totalPart: ${msgMaxCache.size()}")
                    }
                }
                return value
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //8.检查文件
                if (isShouldIntercept(value)) {
                    return value
                }
                CsFileUtils.delete(partDir)
                try {
                    endTaskCheck(tempPath)
                    return value
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
                //9.重命名文件
                if (isShouldIntercept(value)) {
                    return
                }
                CsLogger.tag(tag).i("开始重命名文件. path = $tempPath")
                if (!CsFileUtils.rename(tempPath, task.getFilePath())) {
                    CsLogger.tag(tag)
                        .i("文件重命名失败. srcPath = $tempPath, destPath = ${task.getFilePath()}")
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
                //10.下载结束
                disposable?.dispose()
                CsLogger.tag(tag).i("下载结束. taskTag = ${task.getTaskTag()}")
            }
        })?.start()
    }

    /**
     * 垃圾回收
     * */
    override fun onClear() {
        for (i in 0 until msgMaxCache.size()) {
            latch?.countDown()
        }
    }

    /**
     * 检查下载进度
     * */
    private fun checkProgress(): ITaskDisposable? {
        var disposable: ITaskDisposable? = null
        disposable = CsTask.computationThread()?.delay(1, TimeUnit.SECONDS)
            ?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    val list = msgMaxCache.getAllCache().filter { it.stream != null }

                    if (list.isEmpty()) {
                        return
                    }

                    var tempLength = 0L
                    list.forEach {
                        if (checkFile(it.filePath)) {
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
                        tempLength += CsFileUtils.length(it.filePath)
                    }

                    if (tempLength > 0) {
                        callbackProgress(tempLength)
                    }
                }
            })?.repeat()?.start()
        return disposable
    }

    private var runningNum = AtomicInteger(0)

    /**
     * 尝试发起分片任务
     * */
    private fun tryStartTask() {
        while (runningNum.getAndIncrement() < MAX_PART_RUN) {
            val task = getTaskMsg() ?: return
            startTask(task)
        }
        runningNum.decrementAndGet()
    }

    /**
     * 获取一个还未下载的任务
     * */
    private fun getTaskMsg(): MultiMsg? {
        msgMaxCache.getAllCache().forEach {
            if (it is MultiMsg) {
                if (it.status == STATUS_NORMAL) {
                    it.status = STATUS_START
                    return it
                }
            }
        }
        return null
    }

    /**
     * 执行分片任务
     * */
    private fun startTask(msg: MultiMsg) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                //1.任务开始
                CsLogger.tag(tag)
                    .i("准备下载分片 taskTag: ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                if (CsFileUtils.length(msg.filePath) == msg.partSize) {
                    CsLogger.tag(tag)
                        .i("分片已存在且满足要求 taskTag: ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                    msg.status = STATUS_SUCCESS
                    return TASK_FINISH
                }
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //2.创建分片文件, 准备下载
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag)
                    .i("创建分片文件 taskTag = ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                if (!CsFileUtils.createFile(msg.filePath)) {
                    CsLogger.tag(tag)
                        .i("创建分片文件失败 taskTag = ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                    msg.status = STATUS_FAILED
                    return TASK_FINISH
                }
                return TASK_RUNNING
            }
        })?.map(object : IFunction<String, String> {
            override fun apply(value: String): String {
                //3.开始下载
                if (isShouldIntercept(value)) {
                    return value
                }
                CsLogger.tag(tag)
                    .i("开始下载分片 taskTag = ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                try {
                    Task.start(
                        task,
                        msg.filePath,
                        msg.partStartIndex,
                        msg.partSize
                    ) {
                        //得到下载流，用于取消下载
                        if (isCancel()) {
                            CsFileUtils.close(it)
                        } else {
                            msg.stream = it
                        }
                    }
                    if (CsFileUtils.length(msg.filePath) == msg.partSize) {
                        CsLogger.tag(tag)
                            .i("分片下载成功 taskTag = ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                        msg.status = STATUS_SUCCESS
                        return TASK_RUNNING
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(tag).d(throwable)
                }
                CsLogger.tag(tag)
                    .e("分片下载失败 taskTag = ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                //下载失败
                msg.status = STATUS_FAILED
                return TASK_FINISH
            }
        })?.setOnCompleteCallback(object : OnCompleteCallback {
            override fun onCallback() {
                //4.执行后续分片任务
                CsLogger.tag(tag)
                    .i("分片下载结束 taskTag = ${task.getTaskTag()}, part: ${msg.partPosition}, totalPart: ${msgMaxCache.size()}")
                runningNum.decrementAndGet()
                tryStartTask()
                latch?.countDown()
            }
        })?.start()
    }

}