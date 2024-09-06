package com.proxy.service.core.framework.log

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import timber.log.Timber
import timber.log.Timber.Tree
import java.util.regex.Pattern

/**
 * @author: cangHX
 * @data: 2024/4/28 18:53
 * @desc:
 */
object LogInit {

    private var printEnable: Boolean = true

    private var tree: Tree? = null
    private val logCallbacks = ArrayList<LogCallback>()

    fun setIsDebug(isDebug: Boolean) {
        tree = if (isDebug) {
            DebugTree()
        } else {
            ReleaseTree()
        }
        tree?.let {
            Timber.plant(it)
        }
        Timber.plant(NormalTree(logCallbacks))
    }

    fun addLogCallback(callback: LogCallback) {
        logCallbacks.add(callback)
    }

    fun setPrintEnable(enable: Boolean) {
        printEnable = enable
    }

    open class DebugTree : Tree() {

        companion object {
            private const val MAX_LOG_LENGTH = 3000
            private const val MAX_TAG_LENGTH = 23
            private const val CALL_STACK_INDEX = 7
            private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")
        }

        private fun createStackElementTag(element: StackTraceElement): String {
            var tag = element.className
            val m = ANONYMOUS_CLASS.matcher(tag)
            if (m.find()) {
                tag = m.replaceAll("")
            }
            tag = tag.substring(tag.lastIndexOf('.') + 1)
            return if (tag.length <= MAX_TAG_LENGTH) {
                tag
            } else {
                tag.substring(0, MAX_TAG_LENGTH)
            }
        }

        protected fun getClassName(): String {
            val stackTrace = Throwable().stackTrace
            check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements: are you using proguard?" }
            return createStackElementTag(stackTrace[CALL_STACK_INDEX])
        }

        @SuppressLint("LogNotTimber")
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (!printEnable) {
                return
            }

            val tagO = if (TextUtils.isEmpty(tag)) {
                getClassName()
            } else {
                tag
            }

            if (message.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tagO, message)
                } else {
                    Log.println(priority, tagO, message)
                }
                return
            }
            var i = 0
            while (i >= 0 && i < message.length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) {
                    newline
                } else {
                    message.length
                }
                do {
                    val end = newline.coerceAtMost(i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(tagO, part)
                    } else {
                        Log.println(priority, tagO, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }
        }
    }

    class ReleaseTree : DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.DEBUG) {
                return
            }
            super.log(priority, tag, message, t)
        }
    }

    class NormalTree(private val list: ArrayList<LogCallback>) : DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val tagO: String = if (tag.isNullOrEmpty()) {
                getClassName()
            } else {
                tag
            }
            LogPriority.value(priority)?.let {
                list.iterator().forEach { cb ->
                    try {
                        cb.onLog(it, tagO, message, t)
                    } catch (throwable: Throwable) {
                        tree?.log(priority, throwable)
                    }
                }
            }

        }
    }
}