package com.proxy.service.core.framework.io.file.write.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
open class InputStreamSource(private val stream: InputStream?) : AbstractWrite() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}FileWrite_InputStream"
    }

    /**
     * 用于兼容其他来源
     * */
    open fun getSourceStream(): InputStream? {
        return stream
    }

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean, shouldThrow: Boolean): Boolean {
        start(TAG, file.absolutePath)
        val inStream = getSourceStream()

        try {
            if (inStream == null) {
                return false
            }

            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                write(inStream, file, append)
            } else {
                FileOutputStream(file, append).use {
                    write(inStream, it)
                }
            }

            success(TAG, file.absolutePath)
            return true
        } catch (throwable: Throwable) {
            if (shouldThrow) {
                throw throwable
            } else {
                CsLogger.tag(TAG).e(throwable)
            }
        } finally {
            CsFileUtils.close(inStream)
        }
        return false
    }

    override fun writeSync(stream: OutputStream, shouldThrow: Boolean): Boolean {
        val inStream = getSourceStream()

        start(TAG, "OutputStream")
        try {
            if (inStream == null) {
                return false
            }

            write(inStream, stream)
            success(TAG, "OutputStream")
            return true
        } catch (throwable: Throwable) {
            if (shouldThrow) {
                throw throwable
            } else {
                CsLogger.tag(TAG).e(throwable)
            }
        } finally {
            CsFileUtils.close(inStream)
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun write(inStream: InputStream, file: File, append: Boolean) {
        val options = if (append) {
            arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        } else {
            arrayOf(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        }

        Files.newOutputStream(file.toPath(), *options).buffered().use { outputStream ->
            val buffer = ByteArray(IoConfig.IO_BUFFER_SIZE)
            var bytesRead: Int
            while (inStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
        }
    }

    private fun write(inStream: InputStream, outStream: OutputStream) {
        val outputStream = outStream.buffered()
        val buffer = ByteArray(IoConfig.IO_BUFFER_SIZE)
        var bytesRead: Int
        while (inStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.flush()
    }
}