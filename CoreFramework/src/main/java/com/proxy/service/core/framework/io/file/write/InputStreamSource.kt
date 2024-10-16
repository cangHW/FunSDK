package com.proxy.service.core.framework.io.file.write

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.base.IoConfig
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 10:27
 * @desc:
 */
class InputStreamSource(private val stream: InputStream) : AbstractWrite() {

    private val tag = "${Constants.TAG}FileWrite_InputStream"

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean) {
        try {
            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)
            if (append) {
                Files.newOutputStream(
                    file.toPath(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
                ).use { outputStream ->
                    val buffer = ByteArray(IoConfig.BUFFER_SIZE)
                    var bytesRead: Int

                    while ((stream.read(buffer).also { bytesRead = it }) != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                    outputStream.flush()
                }
                return
            }

            Files.copy(
                stream,
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }
}