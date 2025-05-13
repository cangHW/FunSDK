package com.proxy.service.core.framework.io.file.media.source

import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * @author: cangHX
 * @data: 2024/12/31 17:48
 * @desc:
 */
class AutoCloseInputStreamSource(inputStream: InputStream) : InputStreamSource(inputStream) {

    override fun write(file: File): Boolean {
        val flag = super.write(file)
        CsFileUtils.close(inputStream)
        return flag
    }

    override fun write(stream: OutputStream): Boolean {
        val flag = super.write(stream)
        CsFileUtils.close(inputStream)
        return flag
    }
}