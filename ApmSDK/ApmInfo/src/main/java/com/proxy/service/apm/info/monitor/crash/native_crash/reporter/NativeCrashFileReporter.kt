package com.proxy.service.apm.info.monitor.crash.native_crash.reporter

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.crash.native_crash.report.NativeCrashReport
import com.proxy.service.apm.info.report.IReporter
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import java.io.File

class NativeCrashFileReporter(
    private val dir: String
) : IReporter<NativeCrashReport> {

    companion object {
        private const val TAG = "${Constants.TAG}NativeCrashFileReporter"
    }

    override fun publish(time: Long, report: NativeCrashReport) {
        try {
            val timeString = CsTimeManager.createFactory(time).get("yyyyMMdd_HHmmss_SSS")
            val file = File(dir, "$timeString.txt")
            CsFileWriteUtils.setSourceString(report.crashLog).writeSync(file)
            CsLogger.tag(TAG).d("Native Crash report saved: ${file.absolutePath}")
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }
}
