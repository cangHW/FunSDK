package com.proxy.service.logfile.info.utils

import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author: cangHX
 * @data: 2025/1/18 10:10
 * @desc:
 */
object Utils {

    fun getStackTraceString(throwable: Throwable): String {
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        throwable.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}