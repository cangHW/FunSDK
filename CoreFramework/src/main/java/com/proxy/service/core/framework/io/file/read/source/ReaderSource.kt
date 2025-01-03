package com.proxy.service.core.framework.io.file.read.source

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.base.IRead
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
        val bufferedReader = BufferedReader(reader)
        try {
            bufferedReader.lines().use { lines ->
                return lines.collect(Collectors.joining(System.lineSeparator()))
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return ""
    }
}