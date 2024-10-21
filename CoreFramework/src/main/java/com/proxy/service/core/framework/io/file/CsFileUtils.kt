package com.proxy.service.core.framework.io.file

import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.Closeable
import java.io.File
import java.io.Flushable

/**
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileUtils {

    private const val TAG = "${Constants.TAG}File"

    /**
     * 创建文件
     * */
    fun createFile(file: String?): Boolean {
        if (file == null) {
            return false
        }
        return createFile(File(file))
    }

    /**
     * 创建文件
     * */
    fun createFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (file.exists()) {
            return true
        }
        try {
            file.parentFile?.mkdirs()
            return file.createNewFile()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 创建文件夹
     * */
    fun createDir(dir: String?): Boolean {
        if (dir == null) {
            return false
        }
        return createDir(File(dir))
    }

    /**
     * 创建文件夹
     * */
    fun createDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (dir.exists()) {
            return true
        }
        try {
            return dir.mkdirs()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 删除文件或文件夹
     * */
    fun delete(file: String?): Boolean {
        if (file == null) {
            return false
        }
        return delete(File(file))
    }

    /**
     * 删除文件或文件夹
     * */
    fun delete(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!file.exists()) {
            return true
        }
        if (file.isDirectory) {
            file.listFiles()?.let {
                for (child in it) {
                    if (!child.delete()) {
                        return false
                    }
                }
            }
        }
        return file.delete()
    }

    /**
     * 判断是否是文件
     * */
    fun isFile(file: String?): Boolean {
        if (file == null) {
            return false
        }
        return isFile(File(file))
    }

    /**
     * 判断是否是文件
     * */
    fun isFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!file.exists()) {
            return false
        }
        return file.isFile
    }

    /**
     * 判断是否是文件夹
     * */
    fun isDir(dir: String?): Boolean {
        if (dir == null) {
            return false
        }
        return isDir(File(dir))
    }

    /**
     * 判断是否是文件夹
     * */
    fun isDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.exists()) {
            return false
        }
        return dir.isDirectory
    }

    /**
     * 关闭流（字节流、字符流、缓冲流、读写流等等）
     * */
    fun close(stream: Closeable?) {
        try {
            if (stream is Flushable) {
                stream.flush()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        try {
            stream?.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }
}