package com.proxy.service.core.framework.io.file.write.impl

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
open class InputStreamSource(protected val stream: InputStream) : AbstractWrite() {

    private val tag = "${CoreConfig.TAG}FileWrite_InputStream"

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
                arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            } else {
                arrayOf(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            }

            Files.newOutputStream(file.toPath(), *options).buffered().use { outputStream ->
                val buffer = ByteArray(IoConfig.IO_BUFFER_SIZE)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
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