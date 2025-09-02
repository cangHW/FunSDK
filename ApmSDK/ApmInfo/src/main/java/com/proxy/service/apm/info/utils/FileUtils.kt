package com.proxy.service.apm.info.utils

import android.app.Application
import android.text.TextUtils
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/4/14 09:58
 * @desc:
 */
object FileUtils {

    private fun getRootDir(application: Application): String {
        val userDir = CsApmMonitor.getConfig().getRootDir()

        if (!TextUtils.isEmpty(userDir)) {
            return userDir
        }

        val file = application.getExternalFilesDir("apm")
        CsFileUtils.createDir(file)
        return "${file?.absolutePath ?: ""}${File.separator}"
    }

    fun getDefaultDir(application: Application, type: String): String {
        val file = File(getRootDir(application), type)
        CsFileUtils.createDir(file)
        return "${file.absolutePath}${File.separator}"
    }

}