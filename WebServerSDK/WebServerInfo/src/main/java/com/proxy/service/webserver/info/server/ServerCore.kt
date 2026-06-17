package com.proxy.service.webserver.info.server

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.CsNetManager
import com.proxy.service.webserver.base.config.WebServerConfig
import com.proxy.service.webserver.base.constants.Constants
import com.proxy.service.webserver.base.server.ServerInfo
import fi.iki.elonen.NanoHTTPD

/**
 * @author: cangHX
 * @date: 2026/6/16 10:26
 * @desc:
 */
class ServerCore : ServerCoreInterface {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_START}ServerCore"
    }

    @Volatile
    private var server: NanoServer? = null
    private var serverInfo: ServerInfo? = null

    override fun start(config: WebServerConfig): ServerInfo? {
        if (server?.isAlive == true) {
            return serverInfo
        }

        val port = config.getPort()
        val ipAddress = CsNetManager.getActiveNetworkIpAddress()
        val lanUrl = "http://$ipAddress:$port"
        val info = ServerInfo(
            port = port,
            host = "0.0.0.0",
            lanUrl = lanUrl,
            localhostHint = "USB 连接时可执行: adb reverse tcp:$port tcp:$port 后访问 http://localhost:$port",
        )

        val nanoServer = NanoServer(config)
        try {
            nanoServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return null
        }

        server = nanoServer
        serverInfo = info

        CsLogger.tag(TAG).d("NanoHTTPD started: ${info.lanUrl}")
        CsLogger.tag(TAG).d(info.localhostHint)
        return serverInfo
    }

    override fun stop() {
        server?.stop()
        server = null
        serverInfo = null
    }

    override fun isRunning(): Boolean {
        return server?.isAlive == true
    }

    override fun getServerInfo(): ServerInfo? {
        return serverInfo
    }
}

interface ServerCoreInterface {

    fun start(config: WebServerConfig): ServerInfo?

    fun stop()

    fun isRunning(): Boolean

    fun getServerInfo(): ServerInfo?
}
