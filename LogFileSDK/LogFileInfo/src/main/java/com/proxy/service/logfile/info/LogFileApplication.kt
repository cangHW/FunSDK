package com.proxy.service.logfile.info

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.OnAppVisibilityCallback
import com.proxy.service.logfile.info.manager.LogFileCore

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
    }

    override fun onAppBackground() {
        if (LogFileCore.getInstance().isInitSuccess()) {
            LogFileCore.getInstance().flush()
        }
    }

    override fun onAppForeground() {

    }
}