package com.proxy.service.core.framework.io.file.write.source

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.File
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

    private val tag = "${CoreConfig.TAG}FileWrite_Byte"

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean): Boolean {
        start(tag, file.absolutePath)
        try {
            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)

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
            success(tag, file.absolutePath)
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return false
    }

    override fun writeSync(stream: OutputStream, append: Boolean): Boolean {
        start(tag, "OutputStream")
        try {
            val outputStream = stream.buffered()
            val buffer = ByteBuffer.wrap(bytes)
            buffer.flip()
            val temp = ByteArray(IoConfig.IO_BUFFER_SIZE)
            while (buffer.hasRemaining()) {
                val length = Math.min(buffer.remaining(), temp.size)
                buffer.get(temp, 0, length)
                outputStream.write(temp, 0, length)
            }
            outputStream.flush()
            success(tag, "OutputStream")
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return false
    }
}