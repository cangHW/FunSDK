package com.proxy.service.core.framework.io.file.read

import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.IRead
import com.proxy.service.core.framework.io.file.read.source.AutoCloseInputStreamSource
import com.proxy.service.core.framework.io.file.read.source.InputStreamSource
import com.proxy.service.core.framework.io.file.read.source.PathSource
import com.proxy.service.core.framework.io.file.read.source.ReaderSource
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.nio.file.Path
import java.nio.file.Paths

/**
 * IO 读文件工具
 *
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileReadUtils : IRead.Source {

    /**
     * 设置源数据
     *
     * fileName 示例：asd/xxx.txt
     * */
    override fun setSourceAssetPath(fileName: String): IRead {
        val context = CsContextManager.getApplication()
        return AutoCloseInputStreamSource(context.assets.open(fileName))
    }

    /**
     * 设置源数据
     * */
    override fun setSourcePath(filePath: String): IRead {
        return setSourcePath(Paths.get(filePath))
    }

    /**
     * 设置源数据
     * */
    override fun setSourcePath(path: Path): IRead {
        return PathSource(path)
    }

    /**
     * 设置源数据
     * */
    override fun setSourceFile(file: File): IRead {
        return setSourcePath(file.toPath())
    }

    /**
     * 设置源数据
     * */
    override fun setSourceStream(inputStream: InputStream): IRead {
        return InputStreamSource(inputStream)
    }

    /**
     * 设置源数据
     * */
    override fun setSourceReader(reader: Reader): IRead {
        return ReaderSource(reader)
    }

}