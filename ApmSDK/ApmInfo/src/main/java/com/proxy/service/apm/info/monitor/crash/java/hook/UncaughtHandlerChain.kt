package com.proxy.service.apm.info.monitor.crash.java.hook

import android.os.Process
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * Java 崩溃 UncaughtExceptionHandler 链解析与安全转发。
 */
object UncaughtHandlerChain {

    private const val TAG = "${Constants.TAG}UncaughtHandlerChain"

    const val RUNTIME_INIT_HANDLER: String =
        "com.android.internal.os.RuntimeInit\$UncaughtHandler"

    data class HandlerBackups(
        val primary: Thread.UncaughtExceptionHandler?,
        val system: Thread.UncaughtExceptionHandler?,
    )

    fun resolveBackups(prev: Thread.UncaughtExceptionHandler?): HandlerBackups {
        if (prev == null) {
            return HandlerBackups(null, null)
        }
        return if (prev.javaClass.name == RUNTIME_INIT_HANDLER) {
            HandlerBackups(prev, prev)
        } else {
            HandlerBackups(prev, null)
        }
    }

    /**
     * 若当前调用栈已包含该 handler 的 uncaughtException，则不再调用，避免递归。
     */
    private fun canInvokeHandler(handler: Thread.UncaughtExceptionHandler): Boolean {
        val handlerClass = handler.javaClass.name
        for (element in Thread.currentThread().stackTrace) {
            if (element.className == handlerClass &&
                element.methodName == "uncaughtException"
            ) {
                return false
            }
        }
        return true
    }

    fun forward(
        thread: Thread,
        throwable: Throwable,
        primary: Thread.UncaughtExceptionHandler?,
        system: Thread.UncaughtExceptionHandler?,
        skipPrimary: Boolean,
    ) {
        if (!skipPrimary && primary != null && canInvokeHandler(primary)) {
            CsLogger.tag(TAG).d("forward to primary handler")
            primary.uncaughtException(thread, throwable)
            return
        }
        if (system != null && canInvokeHandler(system)) {
            CsLogger.tag(TAG).d("forward to system handler")
            system.uncaughtException(thread, throwable)
            return
        }
        CsLogger.tag(TAG).w("no available downstream handler, terminating process")
        terminateProcess()
    }

    private fun terminateProcess() {
        Process.killProcess(Process.myPid())
        System.exit(10)
    }
}
