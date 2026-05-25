package com.proxy.service.apm.info.monitor.crash

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.common.CommonConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.monitor.crash.java.JavaCrashHookInstaller
import com.proxy.service.apm.info.utils.FileUtils

/**
 * @author: cangHX
 * @date: 2026/5/25 18:09
 * @desc:
 */
class CrashMonitor private constructor() : AbstractMonitor<CommonConfig>() {

    companion object {
        private const val TAG: String = "${Constants.TAG}Crash"

        private val mInstance by lazy { CrashMonitor() }

        fun getInstance(): CrashMonitor {
            return mInstance
        }
    }

    override fun start(application: Application, apmConfig: ApmConfig, config: CommonConfig) {
        JavaCrashHookInstaller.getInstance().install(apmConfig, getLogFileDir(application))
    }

    override fun stop() {
        super.stop()
        JavaCrashHookInstaller.getInstance().uninstall()
    }

    override fun getLogFileDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "crash/")
    }

}