package com.proxy.service.core.framework.file.file

import com.proxy.service.core.framework.log.CsLogger
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/4/28 15:33
 * @desc:
 */
object CsFileUtils {

    /**
     * 创建文件
     * */
    fun createFile(file: String): Boolean {
        return createFile(File(file))
    }

    /**
     * 创建文件
     * */
    fun createFile(file: File): Boolean {
        if (file.exists()) {
            return true
        }
        try {
            file.parentFile?.mkdirs()
            return file.createNewFile()
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return false
    }

    /**
     * 创建文件夹
     * */
    fun createDir(dir: String): Boolean {
        return createDir(File(dir))
    }

    /**
     * 创建文件夹
     * */
    fun createDir(dir: File): Boolean {
        if (dir.exists()) {
            return true
        }
        try {
            return dir.mkdirs()
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
        return false
    }

    /**
     * 删除文件或文件夹
     * */
    fun delete(file: String): Boolean {
        return delete(File(file))
    }

    /**
     * 删除文件或文件夹
     * */
    fun delete(file: File): Boolean {
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
    fun isFile(file: String): Boolean {
        return isFile(File(file))
    }

    /**
     * 判断是否是文件
     * */
    fun isFile(file: File): Boolean {
        if (!file.exists()) {
            return false
        }
        return file.isFile
    }

    /**
     * 判断是否是文件夹
     * */
    fun isDir(dir: String): Boolean {
        return isDir(File(dir))
    }

    /**
     * 判断是否是文件夹
     * */
    fun isDir(dir: File): Boolean {
        if (!dir.exists()) {
            return false
        }
        return dir.isDirectory
    }

}