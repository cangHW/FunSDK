package com.proxy.service.logfile.info

import android.app.Application
import android.os.Process
import android.util.Log
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.log.base.LogTree
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.logfile.info.manager.LogFileCore
import com.proxy.service.logfile.info.utils.Utils

/**
 * @author: cangHX
 * @data: 2025/1/16 11:32
 * @desc:
 */
@CloudApiService(serviceTag = "config/log_file")
class LogFileConfig : CsBaseConfig(), Thread.UncaughtExceptionHandler {

    private var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    private var application: Application? = null

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    override fun priority(): Int {
        return -1000
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        this.application = application
        CsLogger.addLogCallback(logTree)

        LogFileCore.getInstance().init(application, CsLogFile.getLogConfig())

        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    private val logTree = object : LogTree() {
        override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
            if (!LogFileCore.getInstance().isInitSuccess()) {
                return
            }

            when (priority) {
                Log.VERBOSE -> {
                    LogFileCore.getInstance().log("V", tag, message)
                }

                Log.DEBUG -> {
                    LogFileCore.getInstance().log("D", tag, message)
                }

                Log.INFO -> {
                    LogFileCore.getInstance().log("I", tag, message)
                }

                Log.WARN -> {
                    LogFileCore.getInstance().log("W", tag, message)
                }

                Log.ERROR -> {
                    LogFileCore.getInstance().log("E", tag, message)
                    LogFileCore.getInstance().flush()
                }

                Log.ASSERT -> {
                    LogFileCore.getInstance().log("A", tag, message)
                }

                else -> {
                    LogFileCore.getInstance().log("UNKNOWN", tag, message)
                }
            }
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            val msg = "\n\nFATAL EXCEPTION: ${t.name} \n" +
                    "Process: ${application?.packageName}, PID: ${Process.myPid()} \n" +
                    Utils.getStackTraceString(e)

            if (LogFileCore.getInstance().isInitSuccess()) {
                LogFileCore.getInstance().log("E", "AndroidRuntime", msg)
            }
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        } finally {
            if (LogFileCore.getInstance().isInitSuccess()) {
                LogFileCore.getInstance().flush()
            }
        }

        uncaughtExceptionHandler?.uncaughtException(t, e)
    }
}