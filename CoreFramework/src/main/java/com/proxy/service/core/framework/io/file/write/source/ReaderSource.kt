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
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardOpenOption


/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
class ReaderSource(private val reader: Reader) : AbstractWrite() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}FileWrite_Reader"
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
        } finally {
            CsFileUtils.close(reader)
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
        } finally {
            CsFileUtils.close(reader)
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
        val bos = stream.buffered()
        val buffer = CharArray(IoConfig.IO_BUFFER_SIZE)
        var bytesRead: Int

        while ((reader.read(buffer).also { bytesRead = it }) != -1) {
            val byteBuffer = String(buffer, 0, bytesRead).toByteArray(StandardCharsets.UTF_8)
            bos.write(byteBuffer)
        }
        bos.flush()
    }
}