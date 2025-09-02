package com.proxy.service.core.framework.io.file.write.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption


/**
 * @author: cangHX
 * @data: 2024/9/25 15:37
 * @desc:
 */
class ByteSource(private val bytes: ByteArray) : AbstractWrite() {

    companion object{
        private const val TAG = "${CoreConfig.TAG}FileWrite_Byte"
    }

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean, shouldThrow: Boolean): Boolean {
        start(TAG, file.absolutePath)
        try {
            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                write(file, append)
            } else {
                FileOutputStream(file, append).use {
                    write(it)
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
        }
        return false
    }

    override fun writeSync(stream: OutputStream, shouldThrow: Boolean): Boolean {
        start(TAG, "OutputStream")
        try {
            write(stream)
            success(TAG, "OutputStream")
            return true
        } catch (throwable: Throwable) {
            if (shouldThrow) {
                throw throwable
            } else {
                CsLogger.tag(TAG).e(throwable)
            }
        }finally {
            CsFileUtils.close(stream)
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun write(file: File, append: Boolean) {
        val options = if (append) {
            arrayOf(
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND
            )
        } else {
            arrayOf(
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING
            )
        }

        FileChannel.open(file.toPath(), *options).use { channel ->
            val buffer = ByteBuffer.wrap(bytes)
            while (buffer.hasRemaining()) {
                channel.write(buffer)
            }
        }
    }

    private fun write(stream: OutputStream) {
        val outputStream = stream.buffered()
        val temp = ByteArray(IoConfig.IO_BUFFER_SIZE)
        var offset = 0

        while (offset < bytes.size) {
            val length = Math.min(bytes.size - offset, temp.size)
            System.arraycopy(bytes, offset, temp, 0, length)
            outputStream.write(temp, 0, length)
            offset += length
        }
    }

}