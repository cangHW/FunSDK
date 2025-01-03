package com.proxy.service.core.framework.io.file.media.config

import android.os.Environment

/**
 * @author: cangHX
 * @data: 2025/1/2 16:19
 * @desc:
 */
class DataInfo {

    /**
     * ID
     * */
    var id: Long = -1

    /**
     * 文件名称
     * */
    var displayName: String = ""

    /**
     * 文件类型
     * */
    var mimeType: String = ""

    /**
     * 根路径
     * */
    val rootDir = getEnvironmentRootDir()

    /**
     * 绝对路径
     * */
    var path: String = ""

    /**
     * 相对路径
     * */
    var relativePath: String = ""

    /**
     * 添加时间
     * */
    var dateAdded: Long = 0

    /**
     * 是否挂起, 0 未挂起, 1 挂起
     * */
    var isPending: Int = 0

    /**
     * 是否删除, 0 未删除, 1 删除
     * */
    var isTrashed: Int = 0


    private fun getEnvironmentRootDir(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            .parentFile
            ?.absolutePath
            ?: ""
    }

    override fun toString(): String {
        return "DataInfo(id=$id, displayName='$displayName', mimeType='$mimeType', rootDir=$rootDir, path='$path', relativePath='$relativePath', dateAdded=$dateAdded, isPending=$isPending, isTrashed=$isTrashed)"
    }
}