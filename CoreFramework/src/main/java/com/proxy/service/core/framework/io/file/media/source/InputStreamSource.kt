package com.proxy.service.core.framework.io.file.media.source

import com.proxy.service.core.framework.io.file.media.base.ISourceWrite
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.InputStream
import java.io.OutputStream

/**
 * @author: cangHX
 * @data: 2024/12/31 17:23
 * @desc:
 */
open class InputStreamSource(protected val inputStream: InputStream) : ISourceWrite {
    override fun write(stream: OutputStream): Boolean {
        return CsFileWriteUtils.setSourceStream(inputStream).writeSync(stream)
    }
}