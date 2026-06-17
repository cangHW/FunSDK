package com.proxy.service.debugbridge.base.plugin

import java.util.concurrent.ConcurrentHashMap

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
object PluginRegistry {

    private val plugins = ConcurrentHashMap<String, IDebugPlugin>()

    fun register(plugin: IDebugPlugin) {
        plugins[plugin.id] = plugin
    }

    fun get(pluginId: String): IDebugPlugin? {
        return plugins[pluginId]
    }

    fun list(): List<IDebugPlugin> {
        return plugins.values.toList()
    }
}
