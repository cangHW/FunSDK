package com.proxy.service.webview.info.config

import android.webkit.JavascriptInterface
import com.proxy.service.core.framework.log.CsLogger
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/8/1 14:26
 * @desc:
 */
class JavascriptInterfaceMethodInfo(private val tClass: Class<*>) {

    private val init = AtomicBoolean(false)
    private val methods = ArrayList<Method>()

    fun isReady(): Boolean {
        if (init.compareAndSet(false, true)) {
            try {
                checkMethod()
            } catch (throwable: Throwable) {
                CsLogger.e(throwable)
            }
        }
        return true
    }

    fun getMethods(): ArrayList<Method> {
        return methods
    }

    private fun checkMethod() {
        tClass.methods.forEach {
            if (it.getAnnotation(JavascriptInterface::class.java) != null) {
                methods.add(it)
            }
        }
    }

}