package com.proxy.service.core.framework.system.net.controller

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.NetType
import com.proxy.service.core.framework.system.net.CsNetManager
import com.proxy.service.core.framework.system.net.callback.NetConnectChangedListener
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @date: 2024/7/23 10:50
 * @desc:
 */
class NetworkController(
    private val connectivityManager: ConnectivityManager?,
    weakNetMapper: WeakHashMap<NetConnectChangedListener, Any>,
    callback: () -> Unit
) : AbstractController(weakNetMapper, callback) {

    init {
        CsLogger.tag(TAG).i("run NetworkController")
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun start() {
        CsLogger.tag(TAG).i("start")
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun stop() {
        CsLogger.tag(TAG).i("stop")
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    private var networkId: String = ""

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            callNetConnected()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            CsLogger.tag(TAG).i("onLost network= $network")
            networkId = ""
            callNetDisConnected()
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val nId = network.toString()
            if (nId == networkId) {
                return
            }
            networkId = nId
            CsLogger.tag(TAG)
                .i("onCapabilitiesChanged network= $network, networkCapabilities = $networkCapabilities")

            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                callNetChanged(NetType.TRANSPORT_WIFI)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                if (CsNetManager.isMobile2G()) {
                    callNetChanged(NetType.TRANSPORT_CELLULAR_2G)
                } else if (CsNetManager.isMobile3G()) {
                    callNetChanged(NetType.TRANSPORT_CELLULAR_3G)
                } else if (CsNetManager.isMobile4G()) {
                    callNetChanged(NetType.TRANSPORT_CELLULAR_4G)
                } else if (CsNetManager.isMobile5G()) {
                    callNetChanged(NetType.TRANSPORT_CELLULAR_5G)
                } else {
                    callNetChanged(NetType.TRANSPORT_CELLULAR_UNKNOWN)
                }
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                callNetChanged(NetType.TRANSPORT_BLUETOOTH)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                callNetChanged(NetType.TRANSPORT_ETHERNET)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                callNetChanged(NetType.TRANSPORT_VPN)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                callNetChanged(NetType.TRANSPORT_WIFI_AWARE)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                callNetChanged(NetType.TRANSPORT_LOWPAN)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                callNetChanged(NetType.TRANSPORT_USB)
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_THREAD)) {
                callNetChanged(NetType.TRANSPORT_THREAD)
            } else {
                callNetChanged(NetType.TRANSPORT_UNKNOWN)
            }
        }

//        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
//            super.onLinkPropertiesChanged(network, linkProperties)
//        }
    }
}