package com.proxy.service.apm.info.monitor.performance.leak.heap

import android.os.Debug
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.performance.leak.composite.FullCompositeReporter
import com.proxy.service.apm.info.monitor.performance.leak.composite.FullCompositeReporter.Companion
import com.proxy.service.apm.info.monitor.performance.leak.report.MemoryLeakGroup
import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import shark.AndroidObjectInspectors
import shark.AndroidReferenceMatchers
import shark.HeapAnalysisSuccess
import shark.HeapAnalyzer
import shark.Hprof
import shark.HprofHeapGraph
import java.io.File

/**
 * @author: cangHX
 * @date: 2026/6/4 20:26
 * @desc:
 */
class HeapDumper {

    companion object {
        private const val TAG = "${Constants.TAG}HeapDumper"
    }

    fun dump(tempDir: String): HeapDumpResult {
        val hprofFile = File(tempDir, "leak_${System.currentTimeMillis()}.hprof")
        try {
            hprofFile.parentFile?.mkdirs()
            Debug.dumpHprofData(hprofFile.absolutePath)
            CsLogger.tag(TAG).d("Heap dumped: ${hprofFile.absolutePath}")
            return analyze(hprofFile)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return HeapDumpResult.DumpFailed
        } finally {
            CsFileUtils.delete(hprofFile)
        }
    }

    private fun analyze(heapDumpFile: File): HeapDumpResult {
        try {
            Hprof.open(heapDumpFile).use { hprof ->
                val graph = HprofHeapGraph.indexHprof(hprof)
                val analyzer = HeapAnalyzer { step ->
                    CsLogger.tag(TAG).d("Analysis step: ${step.name}")
                }
                val result = analyzer.analyze(
                    heapDumpFile = heapDumpFile,
                    graph = graph,
                    leakingObjectFinder = AppKeyedWeakReferenceFinder,
                    referenceMatchers = AndroidReferenceMatchers.appDefaults,
                    objectInspectors = AndroidObjectInspectors.appDefaults,
                    metadataExtractor = shark.MetadataExtractor.NO_OP
                )
                return when (result) {
                    is HeapAnalysisSuccess -> HeapDumpResult.Success(mapAnalysis(result))
                    else -> {
                        CsLogger.tag(TAG).w("Heap analysis failed: $result")
                        HeapDumpResult.AnalysisFailed
                    }
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return HeapDumpResult.AnalysisFailed
        }
    }

    private fun mapAnalysis(result: HeapAnalysisSuccess): List<MemoryLeakGroup> {
        val groups = ArrayList<MemoryLeakGroup>()
        result.applicationLeaks.forEach { leak ->
            val map = HashMap<String, MemoryLeakGroup>()
            leak.leakTraces.forEach trace@{ trace ->
                val className = trace.leakingObject.className
                if (map.containsKey(className)) {
                    map.get(className)?.let {
                        it.retainedCount += 1
                    }
                } else {
                    val description = trace.leakingObject.labels.joinToString(", ")
                    val group = MemoryLeakGroup(
                        signature = trace.signature,
                        leakedClassName = className,
                        description = description.ifEmpty { className },
                        leakTrace = trace.toString()
                    )
                    map.put(className, group)
                }
            }
            groups.addAll(map.values)
        }
        return groups
    }
}
