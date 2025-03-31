package com.proxy.service.core.framework.io.file.write.source

import android.os.Build
import androidx.annotation.RequiresApi
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.file.config.IoConfig
import java.io.File
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 10:24
 * @desc:
 */
@RequiresApi(Build.VERSION_CODES.O)
class PathSource(private val path: Path) : AbstractWrite() {

    private val tag = "${CoreConfig.TAG}FileWrite_Path"

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean, shouldThrow: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false
        }
        start(tag, file.absolutePath)
        try {
            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)

            val options = if (append) {
                arrayOf(StandardOpenOption.CREATE, StandardOpenOption.APPEND)
            } else {
                arrayOf(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
            }

            Files.newInputStream(path).buffered().use { inputStream ->
                Files.newOutputStream(file.toPath(), *options).buffered().use { outputStream ->
                    val buffer = ByteArray(IoConfig.IO_BUFFER_SIZE)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }

            success(tag, file.absolutePath)
            return true
        } catch (throwable: Throwable) {
            if (shouldThrow) {
                CsLogger.tag(tag).d(throwable)
                throw throwable
            } else {
                CsLogger.tag(tag).e(throwable)
            }
        }
        return false
    }

    override fun writeSync(stream: OutputStream, shouldThrow: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false
        }
        start(tag, "OutputStream")
        try {
            Files.newInputStream(path).buffered().use { inputStream ->
                stream.buffered().use { outputStream ->
                    val buffer = ByteArray(IoConfig.IO_BUFFER_SIZE)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                }
            }

            success(tag, "OutputStream")
            return true
        } catch (throwable: Throwable) {
            if (shouldThrow) {
                CsLogger.tag(tag).d(throwable)
                throw throwable
            } else {
                CsLogger.tag(tag).e(throwable)
            }
        }
        return false
    }
}