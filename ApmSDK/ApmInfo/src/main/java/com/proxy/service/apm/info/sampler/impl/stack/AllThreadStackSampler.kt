package com.proxy.service.apm.info.sampler.impl.stack

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.sampler.SamplerData
import com.proxy.service.apm.info.sampler.base.BaseSampler
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/25 18:42
 * @desc:
 */
class AllThreadStackSampler private constructor(
    private val maxThreads: Int = Constants.MONITOR_CRASH_ALL_THREAD_STACK_MAX_THREAD,
    private val maxDepthPerThread: Int = Constants.MONITOR_CRASH_ALL_THREAD_STACK_MAX_DEPTH_PER_THREAD,
    private val maxTotalChars: Long = Constants.MONITOR_CRASH_ALL_THREAD_STACK_MAX_TOTAL_CHARS
) : BaseSampler<AllThreadStackSamplerData>(
    Long.MAX_VALUE
) {

    companion object {

        private const val TAG = "${Constants.TAG}AllThreadStackSampler"

        fun create(): AllThreadStackSampler {
            return AllThreadStackSampler()
        }
    }

    override fun getTag(): String {
        return TAG
    }

    override fun start() {
//        super.start()
    }

    override fun stop() {
//        super.stop()
    }

    override fun snapshotsInWindow(startMs: Long, endMs: Long): List<SamplerData> {
        return sampleNow()
    }

    override fun capture() {
        val timestampMs = System.currentTimeMillis()
        val data = AllThreadStackSamplerData(timestampMs)
        try {
            val result = dumpAllThreadStacks(Thread.currentThread())
            data.allThreadStacksText = result.text
            data.threadCount = result.threadCount
            data.truncated = result.truncated
        } catch (t: Throwable) {
            if (CoreConfig.isDebug) {
                CsLogger.tag(TAG).w(t)
            }
            data.allThreadStacksText = "(failed to dump all thread stacks: ${t.message})\n"
            data.threadCount = 0
            data.truncated = true
        }
        addData(data)
    }

    private fun dumpAllThreadStacks(crashThread: Thread): DumpResult {
        val raw = Thread.getAllStackTraces()
        if (raw.isEmpty()) {
            return DumpResult("(no thread stacks)\n", 0, false)
        }
        val sorted = raw.entries.sortedWith(
            compareByDescending<Map.Entry<Thread, Array<StackTraceElement>>> { (t, _) ->
                when {
                    t === crashThread -> {
                        3
                    }

                    t.name == "main" -> {
                        2
                    }

                    else -> {
                        1
                    }
                }
            }.thenBy { (t, _) -> t.name ?: "" }
        )
        val sb = StringBuilder()
        var totalChars = 0
        var count = 0
        var truncated = false
        for ((thread, elements) in sorted) {
            if (count >= maxThreads) {
                truncated = true
                break
            }
            val block = formatThreadBlock(thread, crashThread, elements)
            if (totalChars + block.length > maxTotalChars) {
                sb.append("... all thread stacks truncated (size limit)\n")
                truncated = true
                break
            }
            sb.append(block)
            totalChars += block.length
            count++
        }
        return DumpResult(sb.toString(), count, truncated)
    }

    private fun formatThreadBlock(
        thread: Thread,
        crashThread: Thread,
        elements: Array<StackTraceElement>,
    ): String {
        val tags = buildList {
            if (thread === crashThread) {
                add("CRASH")
            }
            if (thread.name == "main") {
                add("MAIN")
            }
        }
        val tagSuffix = if (tags.isEmpty()) {
            ""
        } else {
            " [${tags.joinToString(",")}]"
        }

        val builder = StringBuilder()
        builder.append("--- Thread").append(tagSuffix).append(" ---\n")
        builder.append("name: ").append(thread.name).append('\n')
        builder.append("id: ").append(thread.id).append('\n')
        builder.append("state: ").append(thread.state).append('\n')
        if (thread === crashThread) {
            builder.append("stack:").append(" 见摘要").append('\n')
        } else {
            builder.append("stack:\n").append(formatStackTrace(elements))
        }
        builder.append('\n')
        return builder.toString()
    }

    private fun formatStackTrace(elements: Array<StackTraceElement>): String {
        if (elements.isEmpty()) return "  (no stack trace)\n"
        val sb = StringBuilder()
        val limit = minOf(elements.size, maxDepthPerThread)
        for (i in 0 until limit) {
            val e = elements[i]
            sb.append("  at ")
                .append(e.className).append('.')
                .append(e.methodName).append('(')
                .append(e.fileName ?: "Unknown Source")
            if (e.lineNumber >= 0) {
                sb.append(':').append(e.lineNumber)
            }
            sb.append(")\n")
        }
        if (elements.size > maxDepthPerThread) {
            sb.append("  ... ")
                .append(elements.size - maxDepthPerThread)
                .append(" more frames\n")
        }
        return sb.toString()
    }

    private data class DumpResult(
        val text: String,
        val threadCount: Int,
        val truncated: Boolean,
    )
}