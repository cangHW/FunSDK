package com.proxy.service.logfile.info

import android.app.Application
import android.util.Log
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.log.base.LogTree
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.logfile.info.manager.LogFileManager

/**
 * @author: cangHX
 * @data: 2025/1/16 11:32
 * @desc:
 */
@CloudApiService(serviceTag = "config/log_file")
class LogFileConfig : CsBaseConfig() {

    override fun priority(): Int {
        return -1000
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        CsLogger.addLogCallback(logTree)

        LogFileManager.getInstance().init(application, CsLogFile.getLogConfig())
    }

    private val logTree = object : LogTree() {
        override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
            if (!LogFileManager.getInstance().isInitSuccess()) {
                return
            }

            when (priority) {
                Log.VERBOSE -> {
                    LogFileManager.getInstance().log("V", tag, message)
                }

                Log.DEBUG -> {
                    LogFileManager.getInstance().log("D", tag, message)
                }

                Log.INFO -> {
                    LogFileManager.getInstance().log("I", tag, message)
                }

                Log.WARN -> {
                    LogFileManager.getInstance().log("W", tag, message)
                }

                Log.ERROR -> {
                    LogFileManager.getInstance().log("E", tag, message)
                }

                Log.ASSERT -> {
                    LogFileManager.getInstance().log("A", tag, message)
                }

                else -> {
                    LogFileManager.getInstance().log("UNKNOWN", tag, message)
                }
            }
        }
    }
}