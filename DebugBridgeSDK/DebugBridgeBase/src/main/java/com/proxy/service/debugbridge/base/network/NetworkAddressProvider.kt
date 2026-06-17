package com.proxy.service.debugbridge.base.network

import java.net.Inet4Address
import java.net.NetworkInterface

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc:
 */
object NetworkAddressProvider {

    fun getLanIpAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces() ?: return "127.0.0.1"
            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()
                if (!networkInterface.isUp || networkInterface.isLoopback) {
                    continue
                }
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    if (address is Inet4Address && !address.isLoopbackAddress) {
                        return address.hostAddress ?: "127.0.0.1"
                    }
                }
            }
        } catch (_: Throwable) {
            // ignore
        }
        return "127.0.0.1"
    }

    fun buildAccessInfo(port: Int): AccessInfo {
        val lanIp = getLanIpAddress()
        return AccessInfo(
            port = port,
            host = "0.0.0.0",
            lanUrl = "http://$lanIp:$port",
            localhostHint = "USB 调试可执行: adb reverse tcp:$port tcp:$port 后访问 http://localhost:$port"
        )
    }
}
