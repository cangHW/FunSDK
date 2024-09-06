package com.proxy.service.webview.info.config

/**
 * @author: cangHX
 * @data: 2024/8/1 18:37
 * @desc:
 */
object JavaScriptCache {

    private val javaScriptInfo = HashMap<Class<*>, JavascriptInterfaceMethodInfo>()

    fun contains(tClass: Class<*>): Boolean {
        return javaScriptInfo.contains(tClass)
    }

    fun put(tClass: Class<*>, info: JavascriptInterfaceMethodInfo) {
        javaScriptInfo[tClass] = info
    }

    fun get(tClass: Class<*>): JavascriptInterfaceMethodInfo? {
        return javaScriptInfo[tClass]
    }

}