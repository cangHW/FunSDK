package com.proxy.service.core.framework.file.file

import com.proxy.service.core.framework.log.CsLogger
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileWriteUtils {

    private const val BUFFER_SIZE = 8192

    /**
     * 写文件
     * @param file      文件路径
     * @param data      待写入内容
     * @param append    是否追加写入
     * */
    fun write(file: String, data: String, append: Boolean = false): Boolean {
        return write(File(file), data.toByteArray())
    }

    /**
     * 写文件
     * @param file      文件 file
     * @param data      待写入内容
     * @param append    是否追加写入
     * */
    fun write(file: File, data: String, append: Boolean = false): Boolean {
        return write(file, data.toByteArray())
    }

    /**
     * 写文件
     * @param file      文件路径
     * @param data      待写入内容
     * @param append    是否追加写入
     * */
    fun write(file: String, bytes: ByteArray, append: Boolean = false): Boolean {
        return write(File(file), bytes)
    }

    /**
     * 写文件
     * @param file      文件 file
     * @param data      待写入内容
     * @param append    是否追加写入
     * */
    fun write(file: File, bytes: ByteArray, append: Boolean = false): Boolean {
        try {
            if (CsFileUtils.isDir(file)) {
                CsLogger.i("${file.absolutePath} 不是合法文件，无法写入内容")
                return false
            }

            if (!append) {
                CsFileUtils.delete(file)
            }
            CsFileUtils.createFile(file)

            if (!file.canWrite()) {
                CsLogger.i("${file.absolutePath} 权限问题，无法写入内容")
                return false
            }

            FileOutputStream(file, append).use { fos ->
                BufferedOutputStream(fos).use { bos ->
                    var offset = 0
                    while (offset < bytes.size) {
                        val length = minOf(BUFFER_SIZE, bytes.size - offset)
                        bos.write(bytes, offset, length)
                        offset += length
                    }
                    bos.flush()
                }
            }
            return true
        } catch (throwable: Throwable) {
            CsLogger.e(throwable, "写入出错")
        }
        return false
    }
}