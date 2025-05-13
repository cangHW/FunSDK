package com.proxy.service.apihttp.info.download.worker.task

import androidx.annotation.WorkerThread
import com.proxy.service.apihttp.base.common.DownloadException
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.manager.OkhttpManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.framework.system.net.CsNetManager
import okhttp3.Request
import java.io.InputStream
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author: cangHX
 * @data: 2024/11/1 19:08
 * @desc:
 */
object Task {

    private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}Task"

    /**
     * 下载文件, 分片下载
     * */
    @Throws(Throwable::class)
    @WorkerThread
    fun start(
        task: DownloadTask,
        tempPath: String,
        startIndex: Long,
        partSize: Long,
        callback: (InputStream) -> Unit
    ) {
        val range = if (partSize < Constants.Download.FILE_PART_SIZE) {
            "bytes=${startIndex + CsFileUtils.length(tempPath)}-"
        } else {
            "bytes=${startIndex + CsFileUtils.length(tempPath)}-${startIndex + partSize - 1}"
        }
        realStart(
            task,
            tempPath,
            range,
            callback
        )
    }

    /**
     * 下载文件
     * */
    @Throws(Throwable::class)
    @WorkerThread
    fun start(
        task: DownloadTask,
        tempPath: String,
        callback: (InputStream) -> Unit
    ) {
        realStart(task, tempPath, "bytes=${CsFileUtils.length(tempPath)}-", callback)
    }

    private fun realStart(
        task: DownloadTask,
        tempPath: String,
        range: String,
        callback: (InputStream) -> Unit
    ) {

        if (!CsNetManager.isAvailable()) {
            throw DownloadException.create(DownloadException.NETWORK_ERROR, "无网络")
        }

        val request = Request.Builder()
            .url(task.getDownloadUrl())
            .header("RANGE", range)
            .build()

        val response = try {
            OkhttpManager.getOkhttpClient().newCall(request).execute()
        } catch (exception: UnknownHostException) {
            throw DownloadException.create(DownloadException.UNKNOWN_HOST, exception.message)
        } catch (exception: SocketTimeoutException) {
            throw DownloadException.create(DownloadException.SOCKET_TIME_OUT, exception.message)
        }

        try {
            response.body?.byteStream()?.use { bs ->
                callback(bs)
                CsFileWriteUtils
                    .setSourceStream(bs)
                    .writeSync(tempPath, append = true, shouldThrow = true)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG)
                .i("写文件失败. srcPath = $tempPath")
            if (throwable is SocketException) {
                if (CsNetManager.isAvailable()) {
                    throw DownloadException.create(DownloadException.NETWORK_ERROR, "网络不稳定")
                } else {
                    throw DownloadException.create(DownloadException.NETWORK_ERROR, "无网络")
                }
            } else if (throwable.message?.contains("ENOSPC") == true) {
                throw DownloadException.create(
                    DownloadException.INSUFFICIENT_STORAGE_SPACE,
                    "存储空间不足"
                )
            } else {
                throw DownloadException.create(
                    DownloadException.FILE_WRITE_FAILURE,
                    "写文件失败"
                )
            }
        }
    }

}