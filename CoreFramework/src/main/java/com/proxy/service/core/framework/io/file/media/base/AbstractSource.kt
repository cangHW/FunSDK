package com.proxy.service.core.framework.io.file.media.base

import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.ISource
import com.proxy.service.core.framework.io.file.media.source.AutoCloseInputStreamSource
import com.proxy.service.core.framework.io.file.media.source.InputStreamSource
import com.proxy.service.core.framework.io.file.media.source.PathSource
import com.proxy.service.core.framework.io.file.media.source.ReaderSource
import java.io.File
import java.io.InputStream
import java.io.Reader
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author: cangHX
 * @data: 2024/12/31 17:37
 * @desc:
 */
abstract class AbstractSource<T> : AbstractMedia<T>(), ISource<T> {

    override fun setSourceAssetPath(fileName: String): T {
        val context = CsContextManager.getApplication()
        store.setSource(AutoCloseInputStreamSource(context.assets.open(fileName)))
        return getT()
    }

    override fun setSourcePath(filePath: String): T {
        return setSourcePath(Paths.get(filePath))
    }

    override fun setSourceFile(file: File): T {
        return setSourcePath(file.toPath())
    }

    override fun setSourcePath(path: Path): T {
        store.setSource(PathSource(path))
        return getT()
    }

    override fun setSourceStream(inputStream: InputStream): T {
        store.setSource(InputStreamSource(inputStream))
        return getT()
    }

    override fun setSourceReader(reader: Reader): T {
        store.setSource(ReaderSource(reader))
        return getT()
    }
}