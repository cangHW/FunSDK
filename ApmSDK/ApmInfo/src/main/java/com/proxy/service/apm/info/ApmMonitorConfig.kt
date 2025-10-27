package com.proxy.service.apm.info

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apm.info.monitor.crash.java.JavaCrashMonitor
import com.proxy.service.apm.info.monitor.performance.MainThreadLagMonitor
import com.proxy.service.apm.info.monitor.performance.UiLagMonitor
import com.proxy.service.core.application.base.CsBaseConfig

/**
 * @author: cangHX
 * @data: 2025/4/12 16:31
 * @desc:
 */
@CloudApiService(serviceTag = "config/apm")
class ApmMonitorConfig : CsBaseConfig() {

    @SuppressLint("Range")
    override fun priority(): Int {
        return -999
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        val config = CsApmMonitor.getConfig()
        MainThreadLagMonitor.getInstance().init(application, config.getMainThreadLagMonitorConfig())
        UiLagMonitor.getInstance().init(application, config.getUiLagMonitorConfig())
        JavaCrashMonitor.getInstance().init(application, config.getJavaCrashMonitorConfig())
    }
}