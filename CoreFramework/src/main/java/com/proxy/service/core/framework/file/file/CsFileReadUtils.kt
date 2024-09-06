package com.proxy.service.core.framework.file.file

import com.proxy.service.core.framework.log.CsLogger
import java.io.BufferedReader
import java.io.File
import java.io.FileReader


/**
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileReadUtils {

    fun read(file: String): String {
        return read(File(file))
    }

    fun read(file: File): String {
        if (!CsFileUtils.isFile(file)) {
            return ""
        }
        if (!file.canRead()) {
            CsLogger.i("${file.absolutePath} 权限问题，无法读取内容")
            return ""
        }
        val contentBuilder = StringBuilder()
        try {
            BufferedReader(FileReader(file)).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    if (contentBuilder.isNotEmpty()) {
                        contentBuilder.append(System.lineSeparator())
                    }
                    contentBuilder.append(line)
                }
            }
            return contentBuilder.toString()
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return ""
    }


}