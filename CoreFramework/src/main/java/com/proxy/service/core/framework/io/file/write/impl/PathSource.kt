package com.proxy.service.core.framework.io.file.write.impl

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

/**
 * @author: cangHX
 * @data: 2024/9/25 10:24
 * @desc:
 */
class PathSource(private val path: Path) : AbstractWrite() {

    private val tag = "${Constants.TAG}FileWrite_Path"

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: File, append: Boolean) {
        start(tag, file.absolutePath)
        try {
            CsFileUtils.createDir(file.getParent())
            CsFileUtils.createFile(file)
            if (append) {
                Files.newBufferedReader(path).use { reader ->
                    Files.newBufferedWriter(
                        file.toPath(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                    ).use { writer ->
                        var line: String?
                        while ((reader.readLine().also { line = it }) != null) {
                            writer.write(line)
                            writer.newLine()
                        }
                        writer.flush()
                    }
                }
                success(tag, file.absolutePath)
                return
            }

            Files.copy(
                path,
                file.toPath(),
                StandardCopyOption.REPLACE_EXISTING
            )
            success(tag, file.absolutePath)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }
}