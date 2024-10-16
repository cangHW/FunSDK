package com.proxy.service.webview.bridge.ds.bridge

import android.webkit.JavascriptInterface
import androidx.annotation.Keep
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.bridge.ds.callback.CompletionHandler
import com.proxy.service.webview.bridge.ds.config.Config
import com.proxy.service.webview.info.config.JavaScriptCache
import org.json.JSONObject
import java.lang.reflect.Method


/**
 * @author: cangHX
 * @data: 2024/8/10 15:32
 * @desc:
 */
class WebBridge {

    private var web: IWeb? = null
    private val cache = HashSet<String>()
    private val bridgeMap = HashMap<String, HashMap<Class<*>, Any>>()

    fun put(nameSpace: String, bridge: Any) {
        val cacheKey = "nameSpace_${bridge.javaClass.name.replace(".", "_")}"
        if (!cache.contains(cacheKey)) {
            cache.add(cacheKey)
            var map = bridgeMap[nameSpace]
            if (map == null) {
                map = HashMap()
                bridgeMap[nameSpace] = map
            }
            val tClass: Class<*>
            val obj :Any
            if (bridge is Class<*>) {
                tClass = bridge
                obj = bridge.getDeclaredConstructor().newInstance()
                    ?: throw IllegalArgumentException("创建对象失败，bridge = $bridge")
            } else {
                tClass = bridge.javaClass
                obj = bridge
            }
            if (!map.containsKey(tClass)) {
                map[tClass] = obj
            }
        }
    }

    fun setWeb(web: IWeb?) {
        this.web = web
    }

    private fun printDebugInfo(error: String) {
        CsLogger.tag(Config.TAG).d(error)
        if (Config.isDebug) {
            web?.evaluateJavascript(
                String.format(
                    "alert('DEBUG ERR MSG: %s')",
                    error.replace("'", "\\\\'")
                ),
                null
            )
        }
    }

    @Keep
    @JavascriptInterface
    fun call(methodName: String, argStr: String): String {
        CsLogger.tag(Config.TAG).d("call(methodName: String, argStr: String) methodName = $methodName, argStr = $argStr")

        var error =
            "Js bridge called, but can't find a corresponded JavascriptInterface object, please check your code!"
        val nameStr = parseNamespace(methodName.trim())
        val mtn = nameStr[1]

        val ret = JSONObject()
        ret.put("code", -1)

        val map = getBridges(nameStr[0])
        if (map.size <= 0) {
            printDebugInfo(error)
            return ret.toString()
        }

        var arg: Any? = null
        var callback: String? = null

        try {
            val args = JSONObject(argStr)
            if (args.has("_dscbstub")) {
                callback = args.getString("_dscbstub")
            }
            if (args.has("data")) {
                arg = args["data"]
            }
        } catch (throwable: Throwable) {
            error = "The argument of \"$mtn\" must be a JSON object string!"
            printDebugInfo(error)
            CsLogger.tag(Config.TAG).e(throwable)
            return ret.toString()
        }

        val invokeInfo: InvokeInfo? = getMethod(map, mtn)
        if (invokeInfo == null) {
            error =
                "Not find method \"$mtn\" implementation! please check if the signature or namespace of the method is right."
            printDebugInfo(error)
            return ret.toString()
        }
        invokeInfo.method.isAccessible = true

        try {
            return invoke(ret, invokeInfo, arg, callback)
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
            error = "Call failed：The parameter of \"$mtn\" in Java is invalid."
            printDebugInfo(error)
        }
        return ret.toString()
    }

    private fun invoke(
        ret: JSONObject,
        invokeInfo: InvokeInfo,
        arg: Any?,
        callback: String?
    ): String {
        if (invokeInfo.method.parameterTypes.isEmpty()) {
            val retData = invokeInfo.method.invoke(invokeInfo.obj) as? String?
            ret.put("code", 0)
            ret.put("data", retData)
            return ret.toString()
        }

        if (invokeInfo.method.parameterTypes.size == 1) {
            if (invokeInfo.method.parameterTypes[0] == Any::class.java) {
                val retData = invokeInfo.method.invoke(invokeInfo.obj, arg) as? String
                ret.put("code", 0)
                ret.put("data", retData)
            }else if (invokeInfo.method.parameterTypes[0] == CompletionHandler::class.java){
                invokeInfo.method.invoke(invokeInfo.obj, object : CompletionHandler {
                    override fun setProgressData(value: String) {
                        this@WebBridge.complete(value, false, callback)
                    }

                    override fun complete(value: String) {
                        this@WebBridge.complete(value, true, callback)
                    }

                    override fun complete() {
                        this@WebBridge.complete(null, true, callback)
                    }
                })
            }
            return ret.toString()
        }

        invokeInfo.method.invoke(invokeInfo.obj, arg, object : CompletionHandler {
            override fun setProgressData(value: String) {
                this@WebBridge.complete(value, false, callback)
            }

            override fun complete(value: String) {
                this@WebBridge.complete(value, true, callback)
            }

            override fun complete() {
                this@WebBridge.complete(null, true, callback)
            }
        })

        return ret.toString()
    }

    private fun complete(value: Any?, complete: Boolean, callback: String?) {
        try {
            val ret = JSONObject()
            ret.put("code", 0)
            ret.put("data", value)
            if (callback != null) {
                var script = String.format("%s(%s.data);", callback, ret.toString())
                if (complete) {
                    script += "delete window.$callback"
                }
                web?.evaluateJavascript(script, null)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(Config.TAG).e(throwable)
        }
    }

    private fun getMethod(map: HashMap<Class<*>, Any>, methodName: String): InvokeInfo? {
        map.forEach {
            JavaScriptCache.get(it.key)?.let { info ->
                info.isReady()
                info.getMethods().forEach mt@{ method ->
                    if (method.name != methodName) {
                        return@mt
                    }
                    val parameterTypes = method.parameterTypes
                    if (parameterTypes.size > 2) {
                        return@mt
                    }
                    if (parameterTypes.size == 1 && (parameterTypes[0] != Any::class.java && parameterTypes[0] != CompletionHandler::class.java)) {
                        CsLogger.tag(Config.TAG).d("first parameter should be Any::class.java or CompletionHandler::class.java, but it is ${parameterTypes[0]}")
                        return@mt
                    }
                    if (parameterTypes.size > 1 && (parameterTypes[0] != Any::class.java || parameterTypes[1] != CompletionHandler::class.java)) {
                        CsLogger.tag(Config.TAG).d("first and last parameter should be Any::class.java CompletionHandler::class.java, but it's ${parameterTypes[0]} ${parameterTypes[1]}")
                        return@mt
                    }
                    return InvokeInfo(it.value, method)
                }
            }
        }
        return null
    }

    private fun getBridges(nameSpace: String): HashMap<Class<*>, Any> {
        val map = HashMap<Class<*>, Any>()
        bridgeMap[nameSpace]?.let {
            map.putAll(it)
        }
        GlobalBridgeManager.get(nameSpace).let {
            map.putAll(it)
        }
        return map
    }

    private fun parseNamespace(method: String): Array<String> {
        var mt = method
        val pos = method.lastIndexOf('.')
        var namespace = ""
        if (pos != -1) {
            namespace = method.substring(0, pos)
            mt = method.substring(pos + 1)
        }
        return arrayOf(namespace, mt)
    }

    private data class InvokeInfo(val obj: Any, val method: Method)
}