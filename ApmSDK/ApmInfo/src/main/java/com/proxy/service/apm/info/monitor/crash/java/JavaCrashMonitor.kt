package com.proxy.service.apm.info.monitor.crash.java

import android.app.Application
import android.os.Process
import com.proxy.service.apm.info.cache.CacheManager
import com.proxy.service.apm.info.common.CommonLog
import com.proxy.service.apm.info.config.controller.MonitorConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.framework.system.device.CsDeviceUtils
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

    fun init(application: Application, config: MonitorConfig) {
        if (!config.getEnable()){
            return
        }

        fileDir = FileUtils.getDefaultDir(application, "java_crash/")
        CacheManager.getInstance().startWatch(fileDir, config)
        packageName = application.packageName

        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
    }

    private val exceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
        try {
            val time = System.currentTimeMillis()

            var timeString = CsTimeManager.createFactory(time)
                .get("yyyy-MM-dd HH:mm:ss.SSS")

            val builder = StringBuilder()
            builder.append("Crash 发生时间: $timeString").append("\n")
                .append("app version name: ${CsAppUtils.getVersionName()}").append("\t")
                .append("app version code: ${CsAppUtils.getVersionCode()}").append("\t")
                .append("thread name: ${t.name}").append("\t")
                .append("Process: $packageName").append("\t")
                .append("PID: ${Process.myPid()}")
                .append("\n").append("\n")

            builder.append(Constants.DIVIDER).append("\n")
            builder.append("Crash 位置").append("\n").append("\n")
            builder.append(getStackTraceString(e))
            builder.append("\n").append("\n")

            builder.append(Constants.DIVIDER).append("\n")
            builder.append("设备信息").append("\n").append("\n")
                .append("device brand: ${CsDeviceUtils.getDeviceBrand()}").append("\t")
                .append("device mode: ${CsDeviceUtils.getDeviceModel()}").append("\t")
                .append("device type: ${CsDeviceUtils.getDeviceType().name}").append("\t")
                .append("rom mode: ${CsDeviceUtils.getRomType().name}").append("\t")
                .append("total memory: ${CsDeviceUtils.getTotalMemory()}").append("\t")
                .append("avail memory: ${CsDeviceUtils.getAvailMemory()}").append("\t")
                .append("app use private memory: ${CsDeviceUtils.getAppUsePrivateMemory()}").append("\t")
                .append("device total storage: ${CsDeviceUtils.getDeviceTotalStorage()}").append("\t")
                .append("device avail storage: ${CsDeviceUtils.getDeviceAvailStorage()}").append("\t")
                .append("isRoot: ${CsDeviceUtils.isRoot()}").append("\t")
                .append("\n").append("\n")

            timeString = CsTimeManager.createFactory(time)
                .get("yyyy年MM月dd日HH时mm分ss秒SSS毫秒")
            val file = File(fileDir, "$timeString.txt")

            CsFileWriteUtils.setSourceString(builder.toString()).writeSync(file)

            CommonLog.logAll(file.absolutePath) {
                if (it) {
                    CsLogger.tag(TAG).i("触发 Java Crash，日志记录成功")
                } else {
                    CsLogger.tag(TAG).i("触发 Java Crash，日志记录失败")
                }
                uncaughtExceptionHandler?.uncaughtException(t, e)
            }
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
            uncaughtExceptionHandler?.uncaughtException(t, e)
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