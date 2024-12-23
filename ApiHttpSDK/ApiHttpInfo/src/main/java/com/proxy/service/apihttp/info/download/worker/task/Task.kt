package com.proxy.service.apihttp.info.download.worker.task

import androidx.annotation.WorkerThread
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.manager.OkhttpManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import okhttp3.Request
import java.io.InputStream

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
        val request = Request.Builder()
            .url(task.getDownloadUrl())
            .header("RANGE", range)
            .build()

        val response = OkhttpManager.getOkhttpClient().newCall(request).execute()

        var writeFileSuccess = false

        response.body?.byteStream()?.use { bs ->
            callback(bs)
            writeFileSuccess = CsFileWriteUtils.setSourceStream(bs).writeSync(tempPath, true)
        }

        if (!writeFileSuccess) {
            CsLogger.tag(TAG)
                .i("写文件失败. srcPath = $tempPath")
            throw IllegalArgumentException("写文件失败")
        }
    }

}