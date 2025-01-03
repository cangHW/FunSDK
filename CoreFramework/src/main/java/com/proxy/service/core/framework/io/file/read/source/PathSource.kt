package com.proxy.service.core.framework.io.file.read.source

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
class PathSource(private val path: Path) : IRead {

    private val tag = "${CoreConfig.TAG}FileRead_Path"

    /**
     * 读取全部数据
     * */
    override fun readString(charset: Charset): String {
        try {
            Files.lines(path, charset).use { lines ->
                return lines.collect(Collectors.joining(System.lineSeparator()))
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return ""
    }
}