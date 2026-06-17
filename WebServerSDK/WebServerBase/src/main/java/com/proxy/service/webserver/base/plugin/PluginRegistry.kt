package com.proxy.service.webserver.base.plugin

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webserver.base.constants.Constants
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
object PluginRegistry {

    private const val TAG = "${Constants.LOG_TAG_START}PluginRegistry"

    private val plugins = ConcurrentHashMap<String, IWebServerPlugin>()

    /**
     * 注册插件
     *
     * @return 注册成功返回 true；同 pluginId 已存在时打日志并返回 false
     */
    fun register(plugin: IWebServerPlugin): Boolean {
        val pluginId = plugin.getPluginId()
        if (pluginId.isBlank()) {
            CsLogger.tag(TAG).e("register failed: pluginId is blank")
            return false
        }
        val previous = plugins.putIfAbsent(pluginId, plugin)
        if (previous != null) {
            CsLogger.tag(TAG).w("register failed: pluginId already exists: $pluginId")
            return false
        }
        return true
    }

    fun unregister(pluginId: String): Boolean {
        if (pluginId.isBlank()) {
            return false
        }
        return plugins.remove(pluginId) != null
    }

    fun get(pluginId: String?): IWebServerPlugin? {
        if (pluginId.isNullOrBlank()) {
            return null
        }
        return plugins[pluginId]
    }

    fun list(): List<IWebServerPlugin> {
        return plugins.values.sortedBy { it.getPluginId() }
    }
}
