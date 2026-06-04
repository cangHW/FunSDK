package com.proxy.service.apm.info.cache

import android.app.Application
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.monitor.info.FileInfo

/**
 * @author: cangHX
 * @date: 2026/6/4 10:11
 * @desc:
 */
object CheckManager {

    private const val TAG = "${Constants.TAG}CheckManager"

    fun checkMonitorFileCache(application: Application?) {
        try {
            val handler = CsApmMonitor.getExceptionHandler() ?: return

            var ctx = application
            if (application == null && CoreConfig.isFrameworkInitFinish) {
                ctx = CsContextManager.getApplication()
            }

            if (ctx == null) {
                return
            }

            val rootFile = FileUtils.getDefaultDir(ctx, "")
            val tempDir = FileUtils.getDefaultDir(ctx, Constants.TEMP_DIR_NAME)
            val fileList = ArrayList<FileInfo>()

            CsFileUtils.listFiles(rootFile)?.forEach {
                if (it.absolutePath.startsWith(tempDir)) {
                    return@forEach
                }
                if (CsFileUtils.isFile(it)) {
                    val info = FileInfo()
                    info.filePath = it.absolutePath
                    info.fileName = it.name
                    info.fileLength = it.length()
                    info.lastModified = it.lastModified()
                    fileList.add(info)
                }
            }

            handler.onException(fileList)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

}