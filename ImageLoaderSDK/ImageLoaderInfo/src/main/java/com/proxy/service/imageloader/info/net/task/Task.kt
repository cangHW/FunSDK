package com.proxy.service.imageloader.info.net.task

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection

/**
 * @author: cangHX
 * @data: 2025/10/14 16:22
 * @desc:
 */
class Task constructor(
    private val connection: HttpURLConnection
) : ITask {

    override fun isSuccessful(): Boolean {
        return try {
            connection.responseCode / 100 == 2
        } catch (e: IOException) {
            false
        }
    }

    override fun bodyByteStream(): InputStream {
        return connection.inputStream
    }

    override fun contentType(): String? {
        return connection.contentType
    }

    override fun error(): String? {
        try {
            if (isSuccessful()) {
                return ""
            }
            val url = connection.url
            val code = connection.getResponseCode()
            val error = getErrorFromConnection(connection)
            return "Unable to fetch $url. Failed with $code\n$error"
        } catch (throwable: Throwable) {
            CsLogger.tag(ImageLoaderConstants.TAG).d(throwable)
            return throwable.message
        }
    }

    override fun close() {
        connection.disconnect()
    }

    @Throws(IOException::class)
    private fun getErrorFromConnection(connection: HttpURLConnection): String {
        val r = BufferedReader(InputStreamReader(connection.errorStream))
        val error = StringBuilder()
        var line: String?

        try {
            while ((r.readLine().also { line = it }) != null) {
                error.append(line).append('\n')
            }
        } finally {
            CsFileUtils.close(r)
        }
        return error.toString()
    }
}