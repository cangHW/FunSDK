package com.proxy.service.debugbridge.nano

import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.debugbridge.base.DebugBridgeService
import com.proxy.service.debugbridge.base.config.DebugBridgeConfig
import com.proxy.service.debugbridge.base.constants.DebugBridgeConstants
import com.proxy.service.debugbridge.base.network.AccessInfo
import com.proxy.service.debugbridge.base.plugin.IDebugPlugin
import com.proxy.service.debugbridge.base.plugin.PluginRegistry
import com.proxy.service.debugbridge.base.session.Decision
import com.proxy.service.debugbridge.base.session.SessionManager
import com.proxy.service.core.framework.app.context.ContextInit
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc: NanoHTTPD 方案服务实现
 */
@CloudApiService(serviceTag = DebugBridgeConstants.SERVICE_TAG)
class DebugBridgeServiceImpl : DebugBridgeService {

    companion object {
        private const val TAG = "${DebugBridgeConstants.LOG_TAG_START}Nano"
    }

    @Volatile
    private var config: DebugBridgeConfig? = null

    override fun init(config: DebugBridgeConfig) {
        this.config = config
        val context = ContextInit.application
        if (context != null) {
            NanoDebugBridgeKernel.init(context, config)
        } else {
            CsLogger.tag(TAG).e("Application context is null, call init after CsCore.init().")
        }
    }

    override fun start(): AccessInfo? {
        val context = ContextInit.application ?: return null
        return NanoDebugBridgeKernel.start(context)
    }

    override fun stop() {
        NanoDebugBridgeKernel.stop()
    }

    override fun isRunning(): Boolean {
        return NanoDebugBridgeKernel.isRunning()
    }

    override fun getAccessInfo(): AccessInfo? {
        return NanoDebugBridgeKernel.getAccessInfo()
    }

    override fun registerPlugin(plugin: IDebugPlugin) {
        PluginRegistry.register(plugin)
    }

    override fun submitSession(
        pluginId: String,
        tag: String,
        payload: String,
        metadata: Map<String, String>
    ): Decision {
        val bridgeConfig = config ?: return Decision.TimeoutPassThrough
        val session = SessionManager.register(pluginId, tag, payload, metadata)
        val accessInfo = getAccessInfo()
        if (accessInfo != null) {
            CsLogger.tag(TAG).d("Breakpoint session: ${accessInfo.sessionUrl(session.pluginId, session.id)}")
        }
        return SessionManager.awaitDecision(session.id, bridgeConfig.getSessionTimeoutMs())
    }
}
