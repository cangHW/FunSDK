package com.proxy.service.webview.info.config

import java.lang.reflect.Method

/**
 * @author: cangHX
 * @data: 2024/8/1 14:26
 * @desc:
 */
class JavascriptInterfaceInfo(private val methodInfo: JavascriptInterfaceMethodInfo, private var any: Any) {

    fun isReady(): Boolean {
        return methodInfo.isReady()
    }

    fun getMethods(): ArrayList<Method> {
        return methodInfo.getMethods()
    }

    fun getObj(): Any {
        return any
    }

}