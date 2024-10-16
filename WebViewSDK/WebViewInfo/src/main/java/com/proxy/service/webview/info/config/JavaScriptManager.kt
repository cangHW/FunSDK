package com.proxy.service.webview.info.config

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.info.utils.WebUtils

/**
 * @author: cangHX
 * @data: 2024/8/1 10:37
 * @desc:
 */
object JavaScriptManager {

    private const val TAG = "${Config.LOG_TAG_START}JSManager"

    const val GLOBAL_BRIDGE_NAME = "_mBridge"

    private val cache = HashSet<String>()
    private val globalJavascriptInterfaceMap = HashMap<String, HashMap<Class<*>, Any>?>()

    fun addGlobalJavascriptInterface(any: Any) {
        addGlobalJavascriptInterface(GLOBAL_BRIDGE_NAME, any)
    }

    fun addGlobalJavascriptInterface(nameSpace: String, any: Any) {
        val cacheKey = "${nameSpace}_${any.javaClass.name.replace(".", "_")}"
        if (cache.contains(cacheKey)) {
            CsLogger.tag(TAG).d("重复加载，any = $any")
            return
        }
        cache.add(cacheKey)
        WebUtils.addJavascriptInterfaceToMap(nameSpace, any, globalJavascriptInterfaceMap,
            error = {
                CsLogger.tag(TAG).e(it, "加载 GlobalJavascriptInterface 失败，any = $any")
            },
            success = {
                CsLogger.tag(TAG).d("加载 GlobalJavascriptInterface 成功，any = $any")
            }
        )
    }

    fun getNameMethods(): HashMap<String, HashMap<Class<*>, Any>?> {
        return globalJavascriptInterfaceMap
    }

}