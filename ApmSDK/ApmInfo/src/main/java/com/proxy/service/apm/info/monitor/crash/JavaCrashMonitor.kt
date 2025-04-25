package com.proxy.service.apm.info.monitor.crash

import android.app.Application
import android.os.Process
import com.proxy.service.apm.info.cache.CacheManager
import com.proxy.service.apm.info.common.CommonLog
import com.proxy.service.apm.info.config.controller.Controller
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author: cangHX
 * @data: 2025/4/22 17:14
 * @desc:
 */
class JavaCrashMonitor {

    companion object {
        private const val TAG: String = "${Constants.TAG}JavaCrash"

        private val mInstance by lazy { JavaCrashMonitor() }

        fun getInstance(): JavaCrashMonitor {
            return mInstance
        }
    }

    private var fileDir: String = ""
    private var packageName = ""

    private var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

    fun init(application: Application, controller: Controller) {
        fileDir = FileUtils.getDefaultDir(application, "java_crash/")
        CacheManager.getInstance().startWatch(fileDir, controller)
        packageName = application.packageName

        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
    }

    private val exceptionHandler = object : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(t: Thread, e: Throwable) {
            val fileName = "${
                CsTimeManager.createFactory()
                    .get("yyyy年MM月dd日HH时mm分ss秒SSS毫秒")
            }.txt"
            val file = File(fileDir, fileName)

            try {
                val builder = StringBuilder()
                builder.append(
                    "Crash 发生时间: ${
                        CsTimeManager.createFactory().get("yyyy-MM-dd HH:mm:ss.SSS")
                    }"
                ).append("\n")
                    .append("thread name: ${t.name}").append("\t")
                    .append("Process: $packageName").append("\t")
                    .append("PID: ${Process.myPid()}")
                    .append("\n").append("\n")

                builder.append(Constants.DIVIDER).append("\n")
                builder.append("Crash 位置").append("\n").append("\n")
                builder.append(getStackTraceString(e))
                builder.append("\n").append("\n")

                CsFileWriteUtils.setSourceString(builder.toString()).writeSync(file)
            } catch (throwable: Throwable) {
                CsLogger.e(throwable)
            }

            CommonLog.logAll(file.absolutePath) {
                CsLogger.tag(TAG)
                    .i("触发 Java Crash，日志记录${if (it) "成功" else "失败"}")
                uncaughtExceptionHandler?.uncaughtException(t, e)
            }
        }
    }

    private fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}