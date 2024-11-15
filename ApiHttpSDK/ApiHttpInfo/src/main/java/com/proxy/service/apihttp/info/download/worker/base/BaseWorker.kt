package com.proxy.service.apihttp.info.download.worker.base

import androidx.annotation.WorkerThread
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.manager.CallbackManager
import com.proxy.service.apihttp.info.download.utils.ThreadUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.system.security.CsMd5Utils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File
import java.io.InputStream
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/11/13 11:26
 * @desc:
 */
abstract class BaseWorker(task: DownloadTask) : BaseCallbackWorker(task) {
    companion object {
        const val TASK_RUNNING = "running"
        const val TASK_FINISH = "finish"
    }

    protected open class BaseTaskMsg {
        @Volatile
        var stream: InputStream? = null

        @Volatile
        var filePath: String = ""
    }

    private val isClear = AtomicBoolean(false)
    private val msgArray = ArrayList<BaseTaskMsg>()

    /**
     * 添加下载任务信息
     * */
    protected fun addTaskMsg(msg: BaseTaskMsg) {
        ThreadUtils.checkCurrentThread()
        msgArray.add(msg)
    }

    /**
     * 获取全部任务信息
     * */
    protected fun getAllTaskMsg(): ArrayList<BaseTaskMsg> {
        return ArrayList(msgArray)
    }

    /**
     * 获取任务数量
     * */
    protected fun getTaskNum(): Int {
        return msgArray.size
    }

    final override fun startTask() {
        ThreadUtils.checkCurrentThread()
        onStartTask()
    }

    /**
     * 开始任务
     * */
    abstract fun onStartTask()

    /**
     * 取消任务
     *
     * @param isNeedCallback    是否需要回调任务取消
     * */
    final override fun cancelTask(taskTag: String, isNeedCallback: Boolean) {
        if (!super.cancelTask(taskTag)) {
            return
        }
        ThreadUtils.checkCurrentThread()
        CsLogger.tag(tag).i("取消任务. taskTag = ${task.getTaskTag()}")
        msgArray.forEach {
            CsFileUtils.close(it.stream)
        }
        if (isNeedCallback) {
            callbackCancel()
        }
    }

    override fun finishTask() {
        super.finishTask()
        if (isClear.compareAndSet(false, true)) {
            onClear()
        }
    }

    /**
     * 垃圾回收
     * */
    abstract fun onClear()

    /**
     * 任务开始, 检查是否存在对应文件, 以及对应文件是否满足要求
     * */
    @WorkerThread
    protected fun preTaskCheck(): Boolean {
        try {
            if (CsFileUtils.length(task.getFilePath()) > 0) {
                endTaskCheck(task.getFilePath())
                //文件已存在, 且满足要求
                CsLogger.tag(tag)
                    .i("文件已存在, 且满足要求. taskTag = ${task.getTaskTag()}, path = ${task.getFilePath()}")

                CsTask.mainThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        CallbackManager.getDownloadCallbacks(task.getTaskTag()).forEach {
                            it.onSuccess(task)
                        }
                        return ""
                    }
                })?.start()

                return true
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(
                throwable,
                "文件已存在, 但不满足要求, 准备删除并重新下载. taskTag = ${task.getTaskTag()}, path = ${task.getFilePath()}"
            )
            CsFileUtils.delete(task.getFilePath())
        }
        return false
    }

    /**
     * 任务结束, 检查文件是否满足要求
     * */
    @Throws(Throwable::class)
    @WorkerThread
    protected fun endTaskCheck(filePath: String) {
        CsLogger.tag(tag).i("开始检查文件长度. path = $filePath")
        if (task.getFileSize() != 0L) {
            if (task.getFileSize() != CsFileUtils.length(filePath)) {
                CsLogger.tag(tag).i("文件长度不一致, 删除对应文件. path = $filePath")
                CsFileUtils.delete(filePath)
                throw IllegalArgumentException("文件长度不一致")
            }
        }

        CsLogger.tag(tag).i("开始检查文件 md5. path = $filePath")
        if (task.getFileMd5().isNotEmpty() && task.getFileMd5().isNotBlank()) {
            if (task.getFileMd5() != CsMd5Utils.getMD5(File(filePath))) {
                CsLogger.tag(tag).i("文件 md5 不一致, 删除对应文件. path = $filePath")
                CsFileUtils.delete(filePath)
                throw IllegalArgumentException("文件 md5 不一致")
            }
        }
    }

    /**
     * 检查文件是否存在, 避免系统问题, 存在 io/write 任务执行过程中, 对应文件被删除, 任务无响应, 无法知道 io 是否完成
     * */
    protected fun checkFile(tempPath: String): Boolean {
        return !CsFileUtils.isFile(tempPath)
                &&
                !CsFileUtils.isFile(task.getFilePath())
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