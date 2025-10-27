package com.proxy.service.imageloader.info.net

import com.proxy.service.core.framework.collections.CsExcellentList
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.info.net.base.AbstractCacheAction
import com.proxy.service.imageloader.info.net.task.ITask
import com.proxy.service.imageloader.info.net.task.Task
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2025/10/14 14:28
 * @desc:
 */
abstract class AbstractNet : AbstractCacheAction() {

    companion object {
        private const val FILE_PREFIX = ".temp"
    }

    private val requests = CsExcellentMap<String, RequestInfo>()

    /**
     * 发起请求任务
     * */
    fun enqueue(key: String, url: String, callback: RequestCallback) {
        var status = RequestInfo.STATUS_UNKNOWN
        requests.runInTransaction {
            var info = requests.get(key)
            if (info == null) {
                info = RequestInfo(key, this)
                if (CsFileUtils.isFile(getFileFromCache(key))) {
                    info.setSuccess()
                }
                requests.putSync(key, info)
            }
            if (info.isRunning()) {
                status = RequestInfo.STATUS_RUNNING
                info.callbacks.putSync(callback)
                return@runInTransaction
            }
            if (info.isSuccess()) {
                if (CsFileUtils.isFile(getFileFromCache(key))) {
                    status = RequestInfo.STATUS_SUCCESS
                    return@runInTransaction
                }
            }
            info.setRunning()
            info.callbacks.putSync(callback)
            status = RequestInfo.STATUS_RUNNING
            doWorker(key, url)
        }

        if (status == RequestInfo.STATUS_RUNNING) {
            return
        }

        if (status == RequestInfo.STATUS_SUCCESS) {
            callback.onSuccess(getFileFromCache(key).absolutePath)
        }
    }

    /**
     * 回调成功
     * */
    private fun callSuccess(key: String) {
        requests.runInTransaction {
            val info = requests.get(key) ?: return@runInTransaction
            info.setSuccess()
        }
    }

    /**
     * 回调失败
     * */
    private fun callFailed(key: String, throwable: Throwable) {
        CsLogger.tag(ImageLoaderConstants.TAG).d(throwable)
        requests.runInTransaction {
            val info = requests.get(key) ?: return@runInTransaction
            info.setFailed()
        }
    }

    /**
     * 创建本地文件名称
     * */
    protected open fun createLocalFileName(key: String, url: String, contentType: String?): String {
        return key
    }

    /**
     * 执行请求
     * */
    private fun doWorker(key: String, url: String) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                var task: ITask? = null
                try {
                    task = createTask(url)
                    if (!task.isSuccessful()) {
                        callFailed(key, IllegalArgumentException(task.error()))
                        return ""
                    }
                } catch (throwable: Throwable) {
                    callFailed(key, throwable)
                    CsFileUtils.close(task)
                    task = null
                }

                if (task == null) {
                    return ""
                }

                val localFileName = createLocalFileName(key, url, task.contentType())

                val tempFile = getFileFromCache("$localFileName$FILE_PREFIX")
                CsFileUtils.delete(tempFile)

                val realFile = getFileFromCache(localFileName)

                try {
                    CsFileWriteUtils.setSourceStream(task.bodyByteStream())
                        .writeSync(tempFile, shouldThrow = true)
                    updateInfoForCache(tempFile.name)

                    CsFileUtils.delete(realFile)
                    if (CsFileUtils.rename(tempFile, realFile)) {
                        updateInfoForCache(realFile.name)
                        callSuccess(key)
                        return ""
                    }

                    CsFileUtils.delete(tempFile)
                    CsFileUtils.delete(realFile)
                    callFailed(
                        key,
                        IOException("Unable to rename file. src=${tempFile.absolutePath}, dest=${realFile.absolutePath}")
                    )
                } catch (throwable: Throwable) {
                    CsFileUtils.delete(tempFile)
                    CsFileUtils.delete(realFile)
                    callFailed(key, throwable)
                } finally {
                    CsFileUtils.close(task)
                }
                return ""
            }
        })?.start()
    }

    private fun createTask(url: String): ITask {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        return Task(connection)
    }

}

private class RequestInfo(private val key: String, private val cache: AbstractCacheAction) {

    companion object {
        const val STATUS_UNKNOWN = 0
        const val STATUS_RUNNING = 1
        const val STATUS_SUCCESS = 2
        const val STATUS_FAILED = 3
    }

    private val status = AtomicInteger(STATUS_UNKNOWN)
    val callbacks = CsExcellentList<RequestCallback>()

    fun isSuccess(): Boolean {
        return status.get() == STATUS_SUCCESS
    }

    fun isRunning(): Boolean {
        return status.get() == STATUS_RUNNING
    }

    fun setRunning() {
        status.set(STATUS_RUNNING)
    }

    fun setSuccess() {
        status.set(STATUS_SUCCESS)
        if (callbacks.size() == 0) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callbacks.forEachSync {
                    it.onSuccess(cache.getFileFromCache(key).absolutePath)
                }
                callbacks.clear()
                return ""
            }
        })?.start()
    }

    fun setFailed() {
        status.set(STATUS_FAILED)
        if (callbacks.size() == 0) {
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                callbacks.forEachSync {
                    it.onFailed()
                }
                callbacks.clear()
                return ""
            }
        })?.start()
    }
}