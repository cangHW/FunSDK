package com.proxy.service.webview.info.utils

import com.proxy.service.webview.info.config.JavaScriptCache
import com.proxy.service.webview.info.config.JavascriptInterfaceInfo
import com.proxy.service.webview.info.config.JavascriptInterfaceMethodInfo

/**
 * @author: cangHX
 * @data: 2024/8/1 17:21
 * @desc:
 */
object WebUtils {

    fun addJavascriptInterfaceToMap(
        nameSpace: String,
        any: Any,
        map: HashMap<String, HashMap<Class<*>, Any>?>,
        error: (throwable: Throwable) -> Unit,
        success: () -> Unit
    ) {
        var tClasses = map[nameSpace]
        if (tClasses == null) {
            synchronized(map) {
                tClasses = map[nameSpace]
                if (tClasses == null) {
                    tClasses = HashMap()
                    map[nameSpace] = tClasses
                }
            }
        }
        synchronized(map) {
            try {
                val tClass: Class<*>
                val obj: Any
                if (any is Class<*>) {
                    tClass = any
                    obj = any.getDeclaredConstructor().newInstance()
                        ?: throw IllegalArgumentException("创建对象失败，any = $any")
                } else {
                    tClass = any.javaClass
                    obj = any
                }
                tClasses?.put(tClass, obj)

                if (!JavaScriptCache.contains(tClass)) {
                    JavaScriptCache.put(tClass, JavascriptInterfaceMethodInfo(tClass))
                }
            } catch (throwable: Throwable) {
                error(throwable)
                return
            }
            success()
        }
    }

    fun merge(
        src: HashMap<String, HashMap<Class<*>, Any>?>,
        dest: HashMap<String, HashMap<Class<*>, Any>?>
    ): HashMap<String, ArrayList<JavascriptInterfaceInfo>> {
        val temp = HashMap<String, ArrayList<Class<*>>>()
        val map = HashMap<String, ArrayList<JavascriptInterfaceInfo>>()

        checkAndAddToMap(src, map, temp)
        checkAndAddToMap(dest, map, temp)

        return map
    }

    private fun checkAndAddToMap(
        src: HashMap<String, HashMap<Class<*>, Any>?>,
        map: HashMap<String, ArrayList<JavascriptInterfaceInfo>>,
        temp: HashMap<String, ArrayList<Class<*>>>
    ) {
        src.iterator().forEach { entry ->
            var infos = map[entry.key]
            if (infos == null) {
                infos = ArrayList()
                map[entry.key] = infos
            }

            var tClasses = temp[entry.key]
            if (tClasses == null) {
                tClasses = ArrayList()
                temp[entry.key] = tClasses
            }

            entry.value?.forEach loopV@{
                if (tClasses.contains(it.key)) {
                    return@loopV
                }
                tClasses.add(it.key)
                JavaScriptCache.get(it.key)?.let { info ->
                    if (info.isReady()) {
                        infos.add(JavascriptInterfaceInfo(info, it.value))
                    }
                }
            }
        }
    }

}