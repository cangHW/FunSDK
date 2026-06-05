package com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.observer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Looper
import android.os.Message
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractHook
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.DispatchListener
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * @author: cangHX
 * @date: 2026/5/22 17:42
 * @desc: API 29+ 主 Looper Hook：通过反射安装隐藏 API [Looper.Observer]。
 */
class ObserverHook(
    private val listener: DispatchListener
) : AbstractHook<Looper>() {

    companion object {
        private const val TAG = "${Constants.TAG}ObserverHook"
    }

    private var lastObserverObj: Any? = null
    private var observerProxy: Any? = null
    private var setObserverMethod: Method? = null

    @SuppressLint("PrivateApi")
    override fun start(t: Looper?): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return false
        }

        val looperClass = Looper::class.java

        try {
            val field = looperClass.getField("sObserver")
            field.isAccessible = true
            lastObserverObj = field.get(looperClass)
        } catch (_: Throwable) {
        }

        try {
            val observerClass = Class.forName("android.os.Looper\$Observer")
            observerProxy = Proxy.newProxyInstance(
                observerClass.classLoader,
                arrayOf(observerClass),
                ObserverInvocationHandler(lastObserverObj, listener)
            )
            setObserverMethod = looperClass.getMethod("setObserver", observerClass)
            setObserverMethod?.invoke(t, observerProxy)
            return true
        } catch (_: Throwable) {
        }
        return false
    }

    override fun stop(t: Looper?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return
        }

        try {
            setObserverMethod?.invoke(t, lastObserverObj)
        } catch (_: Throwable) {
        } finally {
            observerProxy = null
        }
    }


    private class ObserverInvocationHandler(
        private val lastObserverObj: Any?,
        private val listener: DispatchListener
    ) : InvocationHandler {

        override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
            when (method?.name) {
                "messageDispatchStarting" -> {
                    listener.onDispatchStart(null)
                    return null
                }

                "messageDispatched" -> {
                    val msg = args?.getOrNull(1) as? Message
                    listener.onDispatchEnd(msg?.toString())
                    return null
                }

                "dispatchingThrewException" -> {
                    val msg = args?.getOrNull(1) as? Message
                    listener.onDispatchEnd(
                        if (msg != null) {
                            "exception: what=${msg.what}"
                        } else {
                            "exception"
                        }
                    )
                    return null
                }
            }

            try {
                lastObserverObj?.let {
                    return method?.invoke(it, args)
                }
            } catch (_: Throwable) {
            }
            return null
        }
    }
}