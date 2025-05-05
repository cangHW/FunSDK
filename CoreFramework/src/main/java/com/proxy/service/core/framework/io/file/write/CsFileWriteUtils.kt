package com.proxy.service.core.framework.io.file.write

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.base.IWrite
import com.proxy.service.core.framework.io.file.write.source.AutoCloseInputStreamSource
import com.proxy.service.core.framework.io.file.write.source.ByteSource
import com.proxy.service.core.framework.io.file.write.source.InputStreamSource
import com.proxy.service.core.framework.io.file.write.source.PathSource
import com.proxy.service.core.framework.io.file.write.source.ReaderSource
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.Reader
import java.nio.file.Path
import java.nio.file.Paths

/**
 * IO 写文件工具
 *
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileWriteUtils : IWrite.Source {

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
     * assetPath 示例：asd/xxx.txt
     * */
    override fun setSourceAssetPath(assetPath: String): IWrite {
        val context = CsContextManager.getApplication()
        return AutoCloseInputStreamSource(context.assets.open(assetPath))
    }

    /**
     * 设置源数据
     * */
    override fun setSourcePath(filePath: String): IWrite {
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
    override fun setSourcePath(path: Path): IWrite {
        return PathSource(path)
    }

    /**
     * 设置源数据
     * */
    override fun setSourceFile(file: File): IWrite {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setSourcePath(file.toPath())
        } else {
            setSourceStream(FileInputStream(file))
        }
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