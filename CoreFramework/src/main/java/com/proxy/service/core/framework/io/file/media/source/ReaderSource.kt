package com.proxy.service.core.framework.io.file.media.source

import com.proxy.service.core.framework.io.file.media.base.ISourceWrite
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.OutputStream
import java.io.Reader

/**
 * @author: cangHX
 * @data: 2024/12/31 17:47
 * @desc:
 */
class ReaderSource(private val reader: Reader) : ISourceWrite {
    override fun write(stream: OutputStream): Boolean {
        return CsFileWriteUtils.setSourceReader(reader).writeSync(stream)
    }
}