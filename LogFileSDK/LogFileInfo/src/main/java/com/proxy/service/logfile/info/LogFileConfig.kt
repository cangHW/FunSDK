package com.proxy.service.logfile.info

import android.app.Application
import android.os.Process
import android.util.Log
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.log.base.LogTree
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.logfile.info.manager.LogFileManager
import com.proxy.service.logfile.info.utils.Utils

/**
 * @author: cangHX
 * @data: 2025/1/16 11:32
 * @desc:
 */
@CloudApiService(serviceTag = "config/log_file")
class LogFileConfig : CsBaseConfig(), Thread.UncaughtExceptionHandler {

    private val uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()

    private var application: Application? = null

    override fun priority(): Int {
        return -1000
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        this.application = application
        CsLogger.addLogCallback(logTree)

        LogFileManager.getInstance().init(application, CsLogFile.getLogConfig())

        Thread.setDefaultUncaughtExceptionHandler(this)
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

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            val msg = "FATAL EXCEPTION: ${t.name} \n" +
                        "Process: ${application?.packageName}, PID: ${Process.myPid()} \n" +
                        Utils.getStackTraceString(e)

            if (LogFileManager.getInstance().isInitSuccess()) {
                LogFileManager.getInstance().log("E", "AndroidRuntime", msg)
            }
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }finally {
            if (LogFileManager.getInstance().isInitSuccess()) {
                LogFileManager.getInstance().flush()
            }
        }

        uncaughtExceptionHandler?.uncaughtException(t, e)
    }
}