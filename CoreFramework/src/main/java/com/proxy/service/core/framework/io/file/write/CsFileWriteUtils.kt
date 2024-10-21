package com.proxy.service.core.framework.io.file.write

import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.IWrite
import com.proxy.service.core.framework.io.file.base.IWriteSource
import com.proxy.service.core.framework.io.file.write.impl.AutoCloseInputStreamSource
import com.proxy.service.core.framework.io.file.write.impl.ByteSource
import com.proxy.service.core.framework.io.file.write.impl.InputStreamSource
import com.proxy.service.core.framework.io.file.write.impl.PathSource
import com.proxy.service.core.framework.io.file.write.impl.ReaderSource
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.nio.file.Paths

/**
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileWriteUtils : IWriteSource {

    /**
     * 设置源数据
     * */
    override fun setSourceString(data: String): IWrite {
        return ByteSource(data.toByteArray())
    }

    /**
     * 设置源数据
     * */
    override fun setSourceByte(bytes: ByteArray): IWrite {
        return ByteSource(bytes)
    }

    /**
     * 设置源数据
     *
     * fileName 示例：asd/xxx.txt
     * */
    override fun setSourceAssetPath(fileName: String): IWrite {
        val context = CsContextManager.getApplication()
        return AutoCloseInputStreamSource(context.assets.open(fileName))
    }

    /**
     * 设置源数据
     * */
    override fun setSourcePath(filePath: String): IWrite {
        return PathSource(Paths.get(filePath))
    }

    /**
     * 设置源数据
     * */
    override fun setSourceFile(file: File): IWrite {
        return PathSource(file.toPath())
    }

    /**
     * 设置源数据
     * */
    override fun setSourceStream(inputStream: InputStream): IWrite {
        return InputStreamSource(inputStream)
    }

    /**
     * 设置源数据
     * */
    override fun setSourceReader(reader: Reader): IWrite {
        return ReaderSource(reader)
    }
}