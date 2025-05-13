package com.proxy.service.core.framework.io.file.write.source

import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2024/10/21 09:51
 * @desc:
 */
class AutoCloseInputStreamSource(stream: InputStream) : InputStreamSource(stream) {

    override fun writeSync(file: File, append: Boolean, shouldThrow: Boolean): Boolean {
        if (!super.writeSync(file, append, shouldThrow)) {
            return false
        }
        CsFileUtils.close(stream)
        return true
    }

}