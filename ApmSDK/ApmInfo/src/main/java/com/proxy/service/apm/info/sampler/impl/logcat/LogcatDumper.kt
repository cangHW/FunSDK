package com.proxy.service.apm.info.sampler.impl.logcat

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayDeque

/**
 * 同步、有界拉取 logcat 缓冲区（`logcat -d -t N`），供 Java Crash 等崩溃路径使用。
 */
object LogcatDumper {

    private const val TAG = "${Constants.TAG}LogcatDumper"

    data class DumpResult(
        val text: String,
        val lineCount: Int,
        val truncated: Boolean,
        val errorHint: String?,
    )

    fun dumpSync(
        maxLines: Int,
        timeoutMs: Long,
        maxBytes: Long,
    ): DumpResult {
        val primary = execDump(
            cmd = arrayOf("logcat", "-d", "-v", "threadtime", "-t", maxLines.toString()),
            maxLines = maxLines,
            timeoutMs = timeoutMs,
            maxBytes = maxBytes
        )
        if (primary.lineCount > 0) {
            return primary
        }
        return execDumpFallbackLastLines(maxLines, timeoutMs, maxBytes)
    }

    private fun execDump(
        cmd: Array<String>,
        maxLines: Int,
        timeoutMs: Long,
        maxBytes: Long
    ): DumpResult {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(cmd)
            return readProcessOutput(
                process,
                maxLines,
                timeoutMs,
                maxBytes,
                null,
            )
        } catch (t: Throwable) {
            CsLogger.tag(TAG).w(t)
            return DumpResult(
                text = "(logcat dump failed: ${t.message})\n",
                lineCount = 0,
                truncated = false,
                errorHint = t.message,
            )
        } finally {
            destroyQuietly(process)
        }
    }

    /**
     * 部分 ROM 不支持 `-t`，降级为 `logcat -d` 并只保留最后 [maxLines] 行。
     */
    private fun execDumpFallbackLastLines(
        maxLines: Int,
        timeoutMs: Long,
        maxBytes: Long,
    ): DumpResult {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(arrayOf("logcat", "-d", "-v", "threadtime"))
            return readProcessOutput(
                process,
                maxLines,
                timeoutMs,
                maxBytes,
                "empty output (fallback -d without -t)",
            )
        } catch (t: Throwable) {
            CsLogger.tag(TAG).w(t)
            return DumpResult(
                text = "(logcat fallback failed: ${t.message})\n",
                lineCount = 0,
                truncated = false,
                errorHint = t.message,
            )
        } finally {
            destroyQuietly(process)
        }
    }

    private fun readProcessOutput(
        process: Process,
        maxLines: Int,
        timeoutMs: Long,
        maxBytes: Long,
        errorHintOnEmpty: String?,
    ): DumpResult {
        val readStart = System.currentTimeMillis()
        val lineWindow = ArrayDeque<String>(maxLines.coerceAtLeast(1))
        var truncated = false
        var totalBytes = 0

        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            while (true) {
                if (System.currentTimeMillis() - readStart > timeoutMs) {
                    truncated = true
                    destroyQuietly(process)
                    return buildResult(
                        lineWindow,
                        truncated,
                        "... logcat read timeout (${timeoutMs}ms)\n",
                        "read timeout ${timeoutMs}ms",
                    )
                }

                val line = reader.readLine() ?: break

                val lineBytes = line.length + 1
                if (totalBytes + lineBytes > maxBytes) {
                    truncated = true
                    destroyQuietly(process)
                    return buildResult(
                        lineWindow,
                        truncated,
                        "... logcat truncated (maxBytes=$maxBytes)\n",
                        "maxBytes limit",
                    )
                }

                if (lineWindow.size >= maxLines) {
                    val removeStr = lineWindow.removeFirst()
                    totalBytes -= (removeStr.length + 1)
                    truncated = true
                }

                lineWindow.addLast(line)
                totalBytes += lineBytes
            }
        }

        val result = waitProcessExit(
            process,
            timeoutMs - (System.currentTimeMillis() - readStart).coerceAtLeast(0)
        )
        if (!result) {
            truncated = true
            destroyQuietly(process)
            return buildResult(
                lineWindow,
                truncated,
                "... logcat process wait timeout\n",
                "process wait timeout",
            )
        }

        if (lineWindow.isEmpty()) {
            return DumpResult(
                text = "(logcat empty, permission denied or unsupported on this device)\n",
                0,
                false,
                errorHintOnEmpty ?: "empty output",
            )
        }

        return buildResult(lineWindow, truncated, suffix = null, errorHint = null)
    }

    private fun buildResult(
        lines: ArrayDeque<String>,
        truncated: Boolean,
        suffix: String?,
        errorHint: String?,
    ): DumpResult {
        val sb = StringBuilder()
        lines.forEach {
            sb.append(it).append('\n')
        }
        suffix?.let {
            sb.append(it)
        }
        return DumpResult(
            text = sb.toString(),
            lineCount = lines.size,
            truncated = truncated,
            errorHint = errorHint,
        )
    }

    private fun waitProcessExit(process: Process, timeoutMs: Long): Boolean {
        if (timeoutMs <= 0) {
            destroyQuietly(process)
            return false
        }
        val deadline = System.currentTimeMillis() + timeoutMs
        while (System.currentTimeMillis() < deadline) {
            try {
                process.exitValue()
                return true
            } catch (_: IllegalThreadStateException) {
                Thread.sleep(10)
            }
        }
        return false
    }

    private fun destroyQuietly(process: Process?) {
        try {
            process?.destroy()
        } catch (_: Throwable) {
        }
    }
}
