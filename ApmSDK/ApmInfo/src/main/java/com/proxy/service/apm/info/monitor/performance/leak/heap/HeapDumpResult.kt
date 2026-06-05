package com.proxy.service.apm.info.monitor.performance.leak.heap

import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakGroup

/**
 * hprof dump 与分析结果。
 */
sealed class HeapDumpResult {

    /** dump 与 Shark 分析均成功。 */
    data class Success(val groups: List<MemoryLeakGroup>) : HeapDumpResult()

    /** [Debug.dumpHprofData] 失败或 hprof 文件不可用。 */
    object DumpFailed : HeapDumpResult()

    /** dump 成功但 Shark 分析失败（含异常与 [shark.HeapAnalysisFailure]）。 */
    object AnalysisFailed : HeapDumpResult()
}
