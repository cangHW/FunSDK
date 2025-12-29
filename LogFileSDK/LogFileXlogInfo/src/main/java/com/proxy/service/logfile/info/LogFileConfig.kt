package com.proxy.service.logfile.info

import android.annotation.SuppressLint
import android.app.Application
import android.os.Process
import android.util.Log
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.api.log.base.LogTree
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.logfile.info.utils.Utils
import com.tencent.mars.xlog.Xlog

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

    @SuppressLint("Range")
    override fun priority(): Int {
        return -1000
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        this.application = application
        CsLogger.addLogCallback(logTree)

//        LogFileCore.getInstance().init(application, CsLogFile.getLogConfig())

        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    private val logTree = object : LogTree() {
        override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
//            if (!LogFileCore.getInstance().isInitSuccess()) {
//                return
//            }

            when (priority) {
                Log.VERBOSE -> {
                    com.tencent.mars.xlog.Log.v(tag, message)
                }

                Log.DEBUG -> {
                    com.tencent.mars.xlog.Log.d(tag, message)
                }

                Log.INFO -> {
                    com.tencent.mars.xlog.Log.i(tag, message)
                }

                Log.WARN -> {
                    com.tencent.mars.xlog.Log.w(tag, message)
                }

                Log.ERROR -> {
                    com.tencent.mars.xlog.Log.e(tag, message)
                    Xlog.flush(false)
                }

                Log.ASSERT -> {
                    com.tencent.mars.xlog.Log.f(tag, message)
                }

                else -> {
                    com.tencent.mars.xlog.Log.v(tag, message)
                }
            }
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            val msg = "\n\nFATAL EXCEPTION: ${t.name} \n" +
                    "Process: ${application?.packageName}, PID: ${Process.myPid()} \n" +
                    Utils.getStackTraceString(e)

            com.tencent.mars.xlog.Log.e("AndroidRuntime", msg)

//            if (LogFileCore.getInstance().isInitSuccess()) {
//                LogFileCore.getInstance().logE("E", "AndroidRuntime", msg)
//            }
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        } finally {
            Xlog.flush(true)
//            if (LogFileCore.getInstance().isInitSuccess()) {
//                LogFileCore.getInstance().flush()
//            }
        }

        uncaughtExceptionHandler?.uncaughtException(t, e)
    }
}