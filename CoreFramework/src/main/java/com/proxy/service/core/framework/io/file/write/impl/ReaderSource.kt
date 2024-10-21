package com.proxy.service.core.framework.io.file.write.impl

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.base.IoConfig
import java.io.File
import java.io.Reader
import java.nio.file.Files
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
class ReaderSource(private val reader: Reader) : AbstractWrite() {

    private val tag = "${Constants.TAG}FileWrite_Reader"

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean) {
        start(tag, file.absolutePath)
        try {
            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)
            val bufferedWriter = if (append) {
                Files.newBufferedWriter(
                    file.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
                )
            } else {
                Files.newBufferedWriter(
                    file.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
                )
            }

            bufferedWriter.use { writer ->
                val buffer = CharArray(IoConfig.BUFFER_SIZE)
                var bytesRead: Int
                while ((reader.read(buffer).also { bytesRead = it }) != -1) {
                    writer.write(buffer, 0, bytesRead)
                }
                writer.flush()
            }
            success(tag, file.absolutePath)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }
}