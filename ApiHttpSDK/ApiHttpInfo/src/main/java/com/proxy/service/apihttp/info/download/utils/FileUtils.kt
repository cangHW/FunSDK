package com.proxy.service.apihttp.info.download.utils

import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.core.framework.app.context.CsContextManager
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/11/4 16:31
 * @desc:
 */
object FileUtils {

    /**
     * 获取默认文件名称
     * */
    fun getDefaultFileName(): String {
        return "${System.currentTimeMillis()}"
    }

    /**
     * 获取默认文件夹
     * */
    fun getDefaultFileDir(): String {
        return "${
            CsContextManager.getApplication().getExternalFilesDir(null)
        }${File.separator}download"
    }

    /**
     * 获取临时文件路径
     * */
    fun getTempPath(realPath: String): String {
        return "$realPath.temp"
    }

    /**
     * 获取分片文件文件夹路径
     * */
    fun getPartDir(task: DownloadTask): String {
        return "${File(task.getFilePath()).parentFile?.absolutePath}${File.separator}${task.getTaskTag()}"
    }

}