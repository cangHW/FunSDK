package com.proxy.service.apm.info.monitor.crash.native_crash

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.common.CommonConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.monitor.crash.native_crash.jni.NativeCrashBridge
import com.proxy.service.apm.info.monitor.crash.native_crash.logcat.CrashLogcatReader
import com.proxy.service.apm.info.monitor.crash.native_crash.report.NativeCrashReport
import com.proxy.service.apm.info.monitor.crash.native_crash.reporter.NativeCrashFileReporter
import com.proxy.service.apm.info.report.composite.CompositeReporter
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.File

class NativeCrashMonitor private constructor() : AbstractMonitor<CommonConfig>() {

    companion object {
        private const val TAG: String = "${Constants.TAG}NativeCrash"
        private const val MARKER_FILE_NAME = "native_crash_marker.tmp"

        private val mInstance by lazy { NativeCrashMonitor() }

        fun getInstance(): NativeCrashMonitor {
            return mInstance
        }
    }

    override fun start(application: Application, apmConfig: ApmConfig, config: CommonConfig) {
        val dir = getLogFileDir(application)
        val tempDir = getTempLogFileDir(application)
        checkPendingCrash(apmConfig, tempDir, dir)

        val ret = NativeCrashBridge.nativeInit(tempDir)
        if (ret != 0) {
            CsLogger.tag(TAG).e("Native crash init failed, code=$ret")
        }
    }

    override fun stop() {
        super.stop()
        NativeCrashBridge.nativeDeinit()
    }

    override fun getLogFileDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "crash/native/")
    }

    private fun getTempLogFileDir(application: Application):String{
        return FileUtils.getDefaultDir(application, "${Constants.TEMP_DIR_NAME}/crash/")
    }

    private fun checkPendingCrash(apmConfig: ApmConfig, tempDir: String, dir: String) {
        val marker = File(tempDir, MARKER_FILE_NAME)
        if (!marker.exists() || marker.length() == 0L) {
            return
        }

        try {
            val lines = marker.readLines()
            if (lines.size < 3) {
                return
            }

            val pid = lines[0].trim().toIntOrNull() ?: return
            val timestampSec = lines[1].trim().toLongOrNull() ?: return
            val signum = lines[2].trim().toIntOrNull() ?: 0

            val crashLog = CrashLogcatReader.readCrashLog(pid)
            if (crashLog.isNullOrBlank()) {
                CsLogger.tag(TAG).w("No crash log found in logcat for pid=$pid")
                return
            }

            val report = NativeCrashReport(
                signum = signum,
                pid = pid,
                crashLog = crashLog
            )

            val reporter = CompositeReporter(
                NativeCrashFileReporter(dir),
                CallbackReporter(apmConfig.getNativeCrashReporter())
            )
            reporter.publish(timestampSec * 1000L, report)
            CsLogger.tag(TAG).d("Pending native crash processed")
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        } finally {
            CsFileUtils.delete(tempDir)
            CsFileUtils.createDir(tempDir)
        }
    }
}
