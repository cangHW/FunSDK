package com.proxy.service.core.framework.io.file.media.source

import com.proxy.service.core.framework.io.file.media.base.ISourceWrite
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.OutputStream

/**
 * @author: cangHX
 * @data: 2025/1/3 15:47
 * @desc:
 */
class ByteSource(private val bytes: ByteArray) : ISourceWrite {
    override fun write(stream: OutputStream): Boolean {
        return CsFileWriteUtils.setSourceByte(bytes).writeSync(stream)
    }
}