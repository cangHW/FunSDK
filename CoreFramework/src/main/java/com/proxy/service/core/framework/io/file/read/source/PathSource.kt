package com.proxy.service.core.framework.io.file.read.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IRead
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

/**
 * @author: cangHX
 * @data: 2024/9/25 10:24
 * @desc:
 */
@RequiresApi(Build.VERSION_CODES.O)
class PathSource(private val path: Path) : IRead {

    companion object {
        private const val TAG = "${CoreConfig.TAG}FileRead_Path"
    }

    /**
     * 读取全部数据
     * */
    override fun readString(charset: Charset): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return ""
        }
        try {
            Files.lines(path, charset).use {
                return it.collect(Collectors.joining(System.lineSeparator()))
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

    override fun readLines(charset: Charset): List<String> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return emptyList()
        }
        try {
            val list = ArrayList<String>()
            Files.lines(path, charset).use { stream ->
                stream.forEach {
                    list.add(it)
                }
            }
            return list
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return emptyList()
    }

    override fun readBytes(): ByteArray {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return ByteArray(0)
        }
        return try {
            Files.newInputStream(path).use { inputStream ->
                inputStream.readBytes()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            ByteArray(0)
        }
    }
}