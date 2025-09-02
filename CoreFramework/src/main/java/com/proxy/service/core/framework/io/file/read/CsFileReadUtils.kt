package com.proxy.service.core.framework.io.file.read

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.IRead
import com.proxy.service.core.framework.io.file.read.source.InputStreamSource
import com.proxy.service.core.framework.io.file.read.source.PathSource
import com.proxy.service.core.framework.io.file.read.source.ReaderSource
import java.io.File
import java.io.FileInputStream
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
     * assetPath 示例：asd/xxx.txt
     * */
    override fun setSourceAssetPath(assetPath: String): IRead {
        val context = CsContextManager.getApplication()
        return setSourceStream(context.assets.open(assetPath))
    }

    /**
     * 设置源数据
     * */
    override fun setSourceUri(uri: Uri): IRead {
        val contentResolver = CsContextManager.getApplication().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        return setSourceStream(inputStream!!)
    }

    /**
     * 设置源数据
     * */
    override fun setSourcePath(filePath: String): IRead {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSourcePath(Paths.get(filePath))
        } else {
            setSourceFile(File(filePath))
        }
    }

    /**
     * 设置源数据
     * */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setSourcePath(path: Path): IRead {
        return PathSource(path)
    }

    /**
     * 设置源数据
     * */
    override fun setSourceFile(file: File): IRead {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSourcePath(file.toPath())
        } else {
            setSourceStream(FileInputStream(file))
        }
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