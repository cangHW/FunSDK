package com.proxy.service.webserver.base.server

/**
 * Web 服务启动后的访问信息
 *
 * @param port 监听端口
 * @param host 绑定地址
 * @param lanUrl 局域网访问根 URL
 * @param localhostHint USB 连接时的 adb reverse 提示
 */
data class ServerInfo(
    val port: Int,
    val host: String,
    val lanUrl: String,
    val localhostHint: String,
)
