package com.proxy.service.logfile.info

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.OnAppShowStatusChangedCallback
import com.proxy.service.logfile.info.manager.LogFileManager

/**
 * @author: cangHX
 * @data: 2025/1/17 14:26
 * @desc:
 */
@CloudApiService(serviceTag = "application/log_file")
class LogFileApplication : CsBaseApplication(), OnAppShowStatusChangedCallback {

    override fun priority(): Int {
        return -500
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CsContextManager.addAppShowStatusChangedCallback(this)
    }

    override fun onAppBackground() {
        if (LogFileManager.getInstance().isInitSuccess()) {
            LogFileManager.getInstance().flush()
        }
    }

    override fun onAppForeground() {

    }
}