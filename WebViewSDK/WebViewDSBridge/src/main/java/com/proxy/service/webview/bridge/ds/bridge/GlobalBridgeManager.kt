package com.proxy.service.webview.bridge.ds.bridge

/**
 * @author: cangHX
 * @data: 2024/8/10 15:47
 * @desc:
 */
object GlobalBridgeManager {

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

    fun get(nameSpace: String): HashMap<Class<*>, Any> {
        return bridgeMap[nameSpace] ?: HashMap()
    }

}