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
     * 文件或文件夹是否存在
     * */
    fun exists(path: String?): Boolean {
        if (path.isNullOrEmpty() || path.isBlank()) {
            return false
        }
        return exists(File(path))
    }

    /**
     * 文件或文件夹是否存在
     * */
    fun exists(file: File?): Boolean {
        if (file == null) {
            return false
        }
        try {
            return file.exists()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 创建文件夹
     * */
    fun createDir(dirPath: String?): Boolean {
        if (dirPath.isNullOrEmpty() || dirPath.isBlank()) {
            return false
        }
        return createDir(File(dirPath))
    }

    /**
     * 创建文件夹
     * */
    fun createDir(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (exists(file)) {
            return true
        }
        try {
            return file.mkdirs()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 创建文件
     * */
    fun createFile(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty() || filePath.isBlank()) {
            return false
        }
        return createFile(File(filePath))
    }

    /**
     * 创建文件
     * */
    fun createFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (exists(file)) {
            return true
        }
        if (!createDir(file.parentFile)) {
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
     * 判断是否是文件夹
     * */
    fun isDir(dirPath: String?): Boolean {
        if (dirPath.isNullOrEmpty() || dirPath.isBlank()) {
            return false
        }
        return isDir(File(dirPath))
    }

    /**
     * 判断是否是文件夹
     * */
    fun isDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!exists(dir)) {
            return false
        }
        try {
            return dir.isDirectory
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 判断是否是文件
     * */
    fun isFile(filePath: String?): Boolean {
        if (filePath.isNullOrEmpty() || filePath.isBlank()) {
            return false
        }
        return isFile(File(filePath))
    }

    /**
     * 判断是否是文件
     * */
    fun isFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!exists(file)) {
            return false
        }
        try {
            return file.isFile
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 获取文件大小
     * */
    fun length(path: String?): Long {
        if (path.isNullOrEmpty() || path.isBlank()) {
            return 0
        }
        return length(File(path))
    }

    /**
     * 获取文件大小
     * */
    fun length(file: File?): Long {
        if (file == null) {
            return 0
        }
        try {
            return file.length()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 删除文件或文件夹
     * */
    fun delete(path: String?): Boolean {
        if (path.isNullOrEmpty() || path.isBlank()) {
            return false
        }
        return delete(File(path))
    }

    /**
     * 删除文件或文件夹
     * */
    fun delete(file: File?): Boolean {
        if (isDir(file)) {
            return deleteDir(file)
        }

        return deleteFile(file)
    }

    private fun deleteFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!exists(file)) {
            return false
        }
        try {
            if (file.canWrite() && file.delete()) {
                return true
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        CsLogger.tag(TAG).e("delete failed. path = ${file.absolutePath}")
        return false
    }

    private fun deleteDir(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (!exists(file)) {
            return false
        }
        try {
            val files = file.listFiles() ?: return deleteFile(file)
            var isDeleteSuccess = true
            for (child in files) {
                if (isDir(child)) {
                    if (!deleteDir(child)) {
                        isDeleteSuccess = false
                    }
                    continue
                }
                if (!deleteFile(child)) {
                    isDeleteSuccess = false
                }
            }
            return isDeleteSuccess && deleteFile(file)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
    }

    /**
     * 重命名文件
     * */
    fun rename(srcFilePath: String?, destFilePath: String?): Boolean {
        if (srcFilePath.isNullOrEmpty() || srcFilePath.isBlank()) {
            return false
        }
        if (destFilePath.isNullOrEmpty() || destFilePath.isBlank()) {
            return false
        }
        return rename(File(srcFilePath), File(destFilePath))
    }

    /**
     * 重命名文件
     * */
    fun rename(srcFile: File?, destFilePath: String?): Boolean {
        if (srcFile == null || !isFile(srcFile)) {
            return false
        }
        if (destFilePath.isNullOrEmpty() || destFilePath.isBlank()) {
            return false
        }
        return rename(srcFile, File(destFilePath))
    }

    /**
     * 重命名文件
     * */
    fun rename(srcFilePath: String?, destFile: File?): Boolean {
        if (srcFilePath.isNullOrEmpty() || srcFilePath.isBlank()) {
            return false
        }
        if (destFile == null || isFile(destFile)) {
            return false
        }
        return rename(File(srcFilePath), destFile)
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
        try {
            return srcFile.renameTo(destFile)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return false
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