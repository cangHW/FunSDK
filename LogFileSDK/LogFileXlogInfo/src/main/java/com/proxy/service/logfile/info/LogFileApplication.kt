package com.proxy.service.logfile.info

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.OnAppVisibilityCallback
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.tencent.mars.xlog.Xlog
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/1/17 14:26
 * @desc:
 */
@CloudApiService(serviceTag = "application/log_file")
class LogFileApplication : CsBaseApplication(), OnAppVisibilityCallback {

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return -1000
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CsContextManager.addAppVisibilityCallback(this)

        val file = application.getExternalFilesDir("xlogfile")
        CsFileUtils.createDir(file)

        val config = Xlog.XLogConfig()
        config.logdir = "${file?.absolutePath ?: ""}${File.separator}"

        Xlog.appenderOpen(config)

    }

    override fun onAppBackground() {
        Xlog.flush(false)
    }

    override fun onAppForeground() {

    }
}