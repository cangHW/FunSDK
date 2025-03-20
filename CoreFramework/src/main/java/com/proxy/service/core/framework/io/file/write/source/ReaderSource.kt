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
import java.io.Reader
import java.nio.CharBuffer
import java.nio.channels.Channels
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.StandardOpenOption


/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
class ReaderSource(private val reader: Reader) : AbstractWrite() {

    private val tag = "${CoreConfig.TAG}FileWrite_Reader"

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean): Boolean {
        start(tag, file.absolutePath)
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

            success(tag, file.absolutePath)
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return false
    }

    override fun writeSync(stream: OutputStream): Boolean {
        start(tag, "OutputStream")
        try {
            write(stream)
            success(tag, "OutputStream")
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun write(file: File, append: Boolean) {
        val options = if (append) {
            arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
        } else {
            arrayOf(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        }

        Files.newBufferedWriter(file.toPath(), *options).use { writer ->
            val buffer = CharArray(IoConfig.IO_BUFFER_SIZE)
            var bytesRead: Int
            while ((reader.read(buffer).also { bytesRead = it }) != -1) {
                writer.write(buffer, 0, bytesRead)
            }
            writer.flush()
        }
    }

    private fun write(stream: OutputStream) {
        Channels.newChannel(stream).use { channel ->
            val encoder = Charset.defaultCharset().newEncoder()
            val charBuffer = CharBuffer.allocate(IoConfig.IO_BUFFER_SIZE)
            while (reader.read(charBuffer) != -1) {
                charBuffer.flip()
                val byteBuffer = encoder.encode(charBuffer)
                channel.write(byteBuffer)
                charBuffer.clear()
            }
        }
    }
}