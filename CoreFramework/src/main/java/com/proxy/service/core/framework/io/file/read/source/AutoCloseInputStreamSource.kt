package com.proxy.service.core.framework.io.file.read.source

import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author: cangHX
 * @data: 2024/10/21 09:51
 * @desc:
 */
class AutoCloseInputStreamSource(stream: InputStream) : InputStreamSource(stream) {

    override fun readString(charset: Charset): String {
        val content = super.readString(charset)
        CsFileUtils.close(stream)
        return content
    }

    override fun readLines(charset: Charset): List<String> {
        val content = super.readLines(charset)
        CsFileUtils.close(stream)
        return content
    }

    override fun readBytes(): ByteArray {
        val content = super.readBytes()
        CsFileUtils.close(stream)
        return content
    }

}