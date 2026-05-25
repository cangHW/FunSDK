package com.proxy.service.apm.info.utils

import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/27 16:13
 * @desc:
 */
object CrashSignatureUtil {

    private const val TAG: String = "${Constants.TAG}CrashSignatureUtil"

    /**
     * 最内层 cause，无 cause 则自身
     * */
    fun rootCause(throwable: Throwable): Throwable {
        var current = throwable
        while (current.cause != null) {
            current = current.cause!!
        }
        return current
    }

    /**
     * 用于去重的签名：异常类型 + 顶栈帧（业务侧优先）。
     */
    fun buildSignature(throwable: Throwable): String {
        try {
            val root = rootCause(throwable)
            val type = root.javaClass.name
            val frame = findSignificantFrame(root.stackTrace) ?: return "$type@unknown"
            val line = if (frame.lineNumber >= 0) {
                frame.lineNumber
            } else {
                0
            }
            // 文件名片段可选，比全类名更短；混淆后仍是 a.b.c 形式
            val location = "${frame.className}.${frame.methodName}:$line"
            return "$type@$location"
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }
        return ""
    }

    /**
     * 优先第一个「非系统/非框架」帧；若全是框架帧则用 stackTrace[0]。
     */
    private fun findSignificantFrame(frames: Array<StackTraceElement>): StackTraceElement? {
        if (frames.isEmpty()) return null
        frames.firstOrNull { !isFrameworkFrame(it) }?.let { return it }
        return frames[0]
    }

    private fun isFrameworkFrame(element: StackTraceElement): Boolean {
        val cn = element.className
        return cn.startsWith("java.")
                || cn.startsWith("javax.")
                || cn.startsWith("android.")
                || cn.startsWith("androidx.")
                || cn.startsWith("com.android.")
                || cn.startsWith("dalvik.")
                || cn.startsWith("kotlin.")
                || cn.startsWith("kotlinx.")
    }

}