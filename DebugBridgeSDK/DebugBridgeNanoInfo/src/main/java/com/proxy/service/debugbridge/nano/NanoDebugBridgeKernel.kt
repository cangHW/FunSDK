package com.proxy.service.debugbridge.nano

import android.content.Context
import com.proxy.service.debugbridge.base.config.DebugBridgeConfig
import com.proxy.service.debugbridge.base.constants.DebugBridgeConstants
import com.proxy.service.debugbridge.base.network.AccessInfo
import com.proxy.service.debugbridge.base.network.NetworkAddressProvider
import com.proxy.service.debugbridge.base.plugin.PluginRegistry
import com.proxy.service.debugbridge.base.session.SessionManager
import com.proxy.service.debugbridge.nano.server.NanoDebugServer
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import fi.iki.elonen.NanoHTTPD

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc: NanoHTTPD 方案服务内核，DebugBridgeServiceImpl 与 Web 路由共用
 */
internal object NanoDebugBridgeKernel {

    private const val TAG = "${DebugBridgeConstants.LOG_TAG_START}NanoKernel"

    @Volatile
    private var config: DebugBridgeConfig? = null

    @Volatile
    private var server: NanoDebugServer? = null

    @Volatile
    private var accessInfo: AccessInfo? = null

    fun init(context: Context, bridgeConfig: DebugBridgeConfig) {
        config = bridgeConfig
        if (bridgeConfig.isAutoStart()) {
            start(context)
        }
    }

    fun start(context: Context): AccessInfo? {
        if (!CoreConfig.isDebug) {
            CsLogger.tag(TAG).e("DebugBridge only works in debug mode.")
            return null
        }
        val bridgeConfig = config ?: return null
        if (server?.isAlive == true) {
            return accessInfo
        }

        val appContext = context.applicationContext
        val info = NetworkAddressProvider.buildAccessInfo(bridgeConfig.getPort())
        val nanoServer = NanoDebugServer(
            appContext,
            bridgeConfig.getPort(),
            bridgeConfig.getAccessToken()
        )
        try {
            nanoServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return null
        }
        server = nanoServer
        accessInfo = info

        CsLogger.tag(TAG).d("NanoHTTPD started: ${info.lanUrl}")
        CsLogger.tag(TAG).d(info.localhostHint)
        return info
    }

    fun stop() {
        server?.stop()
        server = null
        accessInfo = null
        CsLogger.tag(TAG).d("NanoHTTPD stopped.")
    }

    fun isRunning(): Boolean {
        return server?.isAlive == true
    }

    fun getAccessInfo(): AccessInfo? = accessInfo

    fun getConfig(): DebugBridgeConfig? = config

    fun buildSessionJson(): String {
        val sessions = SessionManager.list().map { session ->
            val plugin = PluginRegistry.get(session.pluginId)
            mapOf(
                "id" to session.id,
                "pluginId" to session.pluginId,
                "pluginName" to (plugin?.name ?: session.pluginId),
                "tag" to session.tag,
                "createdAt" to session.createdAt,
                "path" to SessionManager.sessionPath(session.id, session.pluginId)
            )
        }
        return com.proxy.service.core.framework.data.json.CsJsonUtils.toJson(sessions) ?: "[]"
    }

    fun buildPluginsJson(): String {
        val plugins = PluginRegistry.list().map {
            mapOf(
                "id" to it.id,
                "name" to it.name,
                "renderType" to it.renderType,
                "sessionPageAssetPath" to it.sessionPageAssetPath
            )
        }
        return com.proxy.service.core.framework.data.json.CsJsonUtils.toJson(plugins) ?: "[]"
    }
}
