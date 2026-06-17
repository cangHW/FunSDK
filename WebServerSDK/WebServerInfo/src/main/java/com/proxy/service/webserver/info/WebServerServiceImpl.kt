package com.proxy.service.webserver.info

import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.collections.CsExcellentList
import com.proxy.service.webserver.base.WebServerService
import com.proxy.service.webserver.base.config.WebServerConfig
import com.proxy.service.webserver.base.plugin.IWebServerPlugin
import com.proxy.service.webserver.base.plugin.PluginRegistry
import com.proxy.service.webserver.base.plugin.PluginDataResponse
import com.proxy.service.webserver.base.server.ServerInfo
import com.proxy.service.webserver.info.server.ServerCore
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @date: 2026/6/15 17:31
 * @desc:
 */
@CloudApiService(serviceTag = "${CoreConfig.SERVICE_TAG_FUNCTION}/WebServer")
class WebServerServiceImpl : WebServerService {

    private val isInit = AtomicBoolean(false)

    private val core = ServerCore()
    private val startList = CsExcellentList<String>()

    private var config = WebServerConfig.builder().build()


    override fun init(config: WebServerConfig) {
        if (isInit.compareAndSet(false, true)) {
            this.config = config
        }
    }

    override fun start(pluginId: String): ServerInfo? {
        startList.runInTransaction {
            startList.putSync(pluginId)
            if (startList.size() > 0) {
                core.start(config)
            }
        }
        return core.getServerInfo()
    }

    override fun stop(pluginId: String) {
        startList.runInTransaction {
            startList.removeSync(pluginId)
            if (startList.size() <= 0) {
                core.stop()
            }
        }
    }

    override fun isRunning(): Boolean {
        return core.isRunning()
    }

    override fun getServerInfo(): ServerInfo? {
        return core.getServerInfo()
    }

    override fun registerPlugin(plugin: IWebServerPlugin): Boolean {
        return PluginRegistry.register(plugin)
    }

    override fun unregisterPlugin(pluginId: String): Boolean {
        return PluginRegistry.unregister(pluginId)
    }

    override fun submitPluginData(
        pluginId: String,
        payload: String
    ): PluginDataResponse {
        val plugin = PluginRegistry.get(pluginId)
        if (plugin != null) {
            return plugin.handlePluginData(payload)
        }
        return PluginDataResponse.FAILED("plugin not found: $pluginId")
    }
}