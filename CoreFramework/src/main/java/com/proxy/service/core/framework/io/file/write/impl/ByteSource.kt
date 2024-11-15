package com.proxy.service.core.framework.io.file.write.impl

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 15:37
 * @desc:
 */
class ByteSource(private val bytes: ByteArray) : AbstractWrite() {

    private val tag = "${Constants.TAG}FileWrite_Byte"

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
}