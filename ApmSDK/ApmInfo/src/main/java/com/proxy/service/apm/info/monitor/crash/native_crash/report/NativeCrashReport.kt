package com.proxy.service.apm.info.monitor.crash.native_crash.report

class NativeCrashReport(
    val timestampSec: Long,
    val signum: Int,
    val pid: Int,
    val crashLog: String
)
