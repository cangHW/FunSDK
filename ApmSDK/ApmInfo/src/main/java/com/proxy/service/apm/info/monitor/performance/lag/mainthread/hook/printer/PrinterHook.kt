package com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.printer

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Printer
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractHook
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.DispatchListener
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/22 18:06
 * @desc:
 */
class PrinterHook(
    private val listener: DispatchListener
) : AbstractHook<Looper>() {

    companion object {
        private const val TAG = "${Constants.TAG}PrinterHook"
    }

    private var lastPrinterObj: Printer? = null

    @SuppressLint("PrivateApi")
    override fun start(t: Looper?): Boolean {
        val looperClass = Looper::class.java

        try {
            val field = looperClass.getDeclaredField("mLogging")
            field.isAccessible = true
            lastPrinterObj = field.get(t) as? Printer?
        } catch (_: Throwable) {
        }

        val printer = PrinterImpl(lastPrinterObj, listener)
        t?.setMessageLogging(printer)
        return true
    }

    override fun stop(t: Looper?) {
        t?.setMessageLogging(lastPrinterObj)
        lastPrinterObj = null
    }

    private class PrinterImpl(
        private val lastPrinterObj: Printer?,
        private val listener: DispatchListener
    ) : Printer {

        override fun println(x: String?) {
            if (x.isNullOrEmpty()) {
                return
            }
            when (x[0]) {
                '>' -> {
                    listener.onDispatchStart(hint = x)
                }

                '<' -> {
                    listener.onDispatchEnd(hint = x)
                }

                else -> {
                    CsLogger.tag(TAG).w("Unknown looper log: $x")
                }
            }

            lastPrinterObj?.println(x)
        }

    }
}