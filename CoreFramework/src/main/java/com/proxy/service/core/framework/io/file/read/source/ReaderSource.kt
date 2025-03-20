package com.proxy.service.core.framework.io.file.read.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IRead
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.BufferedReader
import java.io.Reader
import java.nio.charset.Charset
import java.util.stream.Collectors


/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
class ReaderSource(private val reader: Reader) : IRead {

    private val tag = "${CoreConfig.TAG}FileRead_Reader"

    /**
     * 读取全部数据
     * */
    override fun readString(charset: Charset): String {
        CsLogger.tag(tag)
            .e("Since the resource comes from the Reader, modifying the Charset again is not supported.")
        try {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                readString26()
            } else {
                readString23()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun readString26(): String {
        val bufferedReader = BufferedReader(reader)
        bufferedReader.lines().use { lines ->
            return lines.collect(Collectors.joining(System.lineSeparator()))
        }
    }

    private fun readString23(): String {
        BufferedReader(reader).use { bufferedReader ->
            val stringBuilder = StringBuilder()
            val buffer = CharArray(IoConfig.IO_BUFFER_SIZE)
            var charsRead: Int

            while (bufferedReader.read(buffer).also { charsRead = it } != -1) {
                stringBuilder.appendRange(buffer, 0, charsRead)
            }

            return stringBuilder.toString()
        }
    }
}