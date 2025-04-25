package com.proxy.service.apm.info

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apm.info.monitor.crash.JavaCrashMonitor
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

    override fun priority(): Int {
        return -999
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        MainThreadLagMonitor.getInstance()
            .init(application, CsApmMonitor.getConfig().getMainThreadLagMonitorController())
        UiLagMonitor.getInstance()
            .init(application, CsApmMonitor.getConfig().getUiLagMonitorController())
        JavaCrashMonitor.getInstance()
            .init(application, CsApmMonitor.getConfig().getJavaCrashMonitorController())
    }
}