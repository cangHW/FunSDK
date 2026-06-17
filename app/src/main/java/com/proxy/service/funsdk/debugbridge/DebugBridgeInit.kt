package com.proxy.service.funsdk.debugbridge

import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.debugbridge.base.DebugBridgeService
import com.proxy.service.debugbridge.base.config.DebugBridgeConfig
import com.proxy.service.debugbridge.base.constants.DebugBridgeConstants

/**
 * debug 构建下启动 DebugBridge Web 服务（打包 assets/debug 页面）。
 * release 调用时 isDebug=false 直接返回，不会加载 DebugBridge 实现类。
 */
object DebugBridgeInit {

    private const val TAG = "${DebugBridgeConstants.LOG_TAG_START}App"

    fun init(isDebug: Boolean) {
        if (!isDebug) {
            return
        }
        val service = CloudSystem.getService(DebugBridgeService::class.java)
        if (service == null) {
            CsLogger.tag(TAG).e("DebugBridgeService not found, add debugImplementation DebugBridgeBase.")
            return
        }
        service.init(
            DebugBridgeConfig.builder()
                .build()
        )
        val accessInfo = service.start()
        if (accessInfo == null) {
            CsLogger.tag(TAG).e("DebugBridge start failed.")
            return
        }
        registerDebugPlugins(service)
        CsLogger.tag(TAG).d("DebugBridge url: ${accessInfo.lanUrl}")
        CsLogger.tag(TAG).d(accessInfo.localhostHint)
    }

    private fun registerDebugPlugins(service: DebugBridgeService) {
        try {
            val clazz = Class.forName("com.proxy.service.apihttp.debug.ApiHttpDebug")
            clazz.getMethod("registerPlugins", DebugBridgeService::class.java).invoke(null, service)
        } catch (_: Throwable) {
            // ApiHttpDebug 未接入时忽略
        }
    }
}
