package com.proxy.service.core.framework.io.file

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.Closeable
import java.io.File
import java.io.Flushable

/**
 * IO 文件操作工具
 *
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileUtils {

    private const val TAG = "${CoreConfig.TAG}File"

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
        if (!createDir(file.parentFile)){
            return false
        }
        try {
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
     * 获取文件大小
     * */
    fun length(path: String?): Long {
        if (path == null) {
            return 0
        }
        return length(File(path))
    }

    /**
     * 获取文件大小
     * */
    fun length(file: File?): Long {
        return file?.length() ?: 0
    }

    /**
     * 重命名文件
     * */
    fun rename(srcPath: String?, destPath: String?): Boolean {
        if (srcPath.isNullOrEmpty() || srcPath.isBlank()) {
            return false
        }
        if (destPath.isNullOrEmpty() || destPath.isBlank()) {
            return false
        }
        return rename(File(srcPath), File(destPath))
    }

    /**
     * 重命名文件
     * */
    fun rename(srcFile: File?, destPath: String?): Boolean {
        if (srcFile == null || !isFile(srcFile)) {
            return false
        }
        if (destPath.isNullOrEmpty() || destPath.isBlank()) {
            return false
        }
        return rename(srcFile, File(destPath))
    }

    /**
     * 重命名文件
     * */
    fun rename(srcPath: String?, destFile: File?): Boolean {
        if (srcPath.isNullOrEmpty() || srcPath.isBlank()) {
            return false
        }
        if (destFile == null || isFile(destFile)) {
            return false
        }
        return rename(File(srcPath), destFile)
    }

    /**
     * 重命名文件
     * */
    fun rename(srcFile: File?, destFile: File?): Boolean {
        if (srcFile == null || !isFile(srcFile)) {
            return false
        }
        if (destFile == null || isFile(destFile)) {
            return false
        }
        return srcFile.renameTo(destFile)
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
            CsLogger.tag(TAG).d(throwable)
        }
        try {
            stream?.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
    }
}