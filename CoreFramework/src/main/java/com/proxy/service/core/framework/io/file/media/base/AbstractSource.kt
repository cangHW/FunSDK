package com.proxy.service.core.framework.io.file.media.base

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.ISource
import com.proxy.service.core.framework.io.file.media.source.InputStreamSource
import com.proxy.service.core.framework.io.file.media.source.PathSource
import com.proxy.service.core.framework.io.file.media.source.ReaderSource
import java.io.File
import java.io.FileInputStream
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

    /**
     * 设置源数据
     * */
    override fun setSourceAssetPath(assetPath: String): T {
        val context = CsContextManager.getApplication()
        store.setSource(InputStreamSource(context.assets.open(assetPath)))
        return getT()
    }

    /**
     * 设置源数据
     * */
    override fun setSourceUri(uri: Uri): T {
        val contentResolver = CsContextManager.getApplication().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        store.setSource(InputStreamSource(inputStream!!))
        return getT()
    }

    /**
     * 设置源数据
     * */
    override fun setSourcePath(filePath: String): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSourcePath(Paths.get(filePath))
        } else {
            setSourceFile(File(filePath))
        }
    }

    /**
     * 设置源数据
     * */
    override fun setSourceFile(file: File): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSourcePath(file.toPath())
        } else {
            setSourceStream(FileInputStream(file))
        }
    }

    /**
     * 设置源数据
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setSourcePath(path: Path): T {
        store.setSource(PathSource(path))
        return getT()
    }

    /**
     * 设置源数据
     * */
    override fun setSourceStream(inputStream: InputStream): T {
        store.setSource(InputStreamSource(inputStream))
        return getT()
    }

    /**
     * 设置源数据
     * */
    override fun setSourceReader(reader: Reader): T {
        store.setSource(ReaderSource(reader))
        return getT()
    }
}