package com.proxy.service.core.framework.io.file.media.source

import com.proxy.service.core.framework.io.file.media.base.ISourceWrite
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.OutputStream
import java.nio.file.Path

/**
 * @author: cangHX
 * @data: 2024/12/31 17:44
 * @desc:
 */
class PathSource(private val path: Path) : ISourceWrite {
    override fun write(stream: OutputStream): Boolean {
        return CsFileWriteUtils.setSourcePath(path).writeSync(stream)
    }
}