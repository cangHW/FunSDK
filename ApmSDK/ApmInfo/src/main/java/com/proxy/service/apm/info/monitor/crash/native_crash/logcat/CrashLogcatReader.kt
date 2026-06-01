package com.proxy.service.apm.info.monitor.crash.native_crash.logcat

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.BufferedReader
import java.io.InputStreamReader

object CrashLogcatReader {

    private const val TAG = "${Constants.TAG}CrashLogcatReader"

    private const val MARKER_START = "*** *** *** ***"
    private const val MARKER_PID = "pid:"

    fun readCrashLog(crashPid: Int): String? {
        if (crashPid <= 0) {
            return null
        }

        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(
                arrayOf("logcat", "-b", "crash", "-d", "-v", "threadtime")
            )
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = extractCrashBlock(reader, crashPid)
            reader.close()
            return result
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return null
        } finally {
            process?.destroy()
        }
    }

    private fun extractCrashBlock(reader: BufferedReader, targetPid: Int): String? {
        val block = StringBuilder()
        var inBlock = false
        var pidMatched = false
        var found: String? = null

        var line = reader.readLine()
        while (line != null) {
            if (line.contains(MARKER_START)) {
                if (inBlock && pidMatched && block.isNotEmpty()) {
                    found = block.toString()
                }
                block.clear()
                inBlock = true
                pidMatched = false
            }

            if (inBlock) {
                block.appendLine(line)

                if (!pidMatched && line.contains(MARKER_PID)) {
                    pidMatched = parsePidFromLine(line) == targetPid
                    if (!pidMatched) {
                        inBlock = false
                        block.clear()
                    }
                }
            }

            line = reader.readLine()
        }

        if (inBlock && pidMatched && block.isNotEmpty()) {
            found = block.toString()
        }

        return found
    }

    private fun parsePidFromLine(line: String): Int {
        val pidIdx = line.indexOf("pid:")
        if (pidIdx < 0) {
            return -1
        }

        val afterPid = line.substring(pidIdx + 4).trimStart()
        val numEnd = afterPid.indexOfFirst {
            !it.isDigit()
        }
        val pidStr = if (numEnd > 0) {
            afterPid.substring(0, numEnd)
        } else {
            afterPid
        }

        return pidStr.toIntOrNull() ?: -1
    }
}
