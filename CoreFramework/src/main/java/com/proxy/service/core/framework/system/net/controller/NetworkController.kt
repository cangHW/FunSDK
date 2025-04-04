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
 * @data: 2024/7/23 10:50
 * @desc:
 */
class NetworkController(
    private val connectivityManager: ConnectivityManager?,
    private val weakNetMapper: WeakHashMap<NetConnectChangedListener, Any>,
    private val callback: () -> Unit
) : IController {

    init {
        CsLogger.tag(IController.TAG).i("run NetworkController")
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    override fun start() {
        CsLogger.tag(IController.TAG).i("start")
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun stop() {
        CsLogger.tag(IController.TAG).i("stop")
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    private var networkId: String = ""

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            synchronized(weakNetMapper) {
                weakNetMapper.keys.forEach {
                    IController.runUiThread {
                        it.onNetConnected()
                    }
                }
                callback()
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            CsLogger.tag(IController.TAG).i("onLost network= $network")
            networkId = ""
            synchronized(weakNetMapper) {
                weakNetMapper.keys.forEach {
                    IController.runUiThread {
                        it.onNetDisConnected()
                    }
                }
                callback()
            }
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
            CsLogger.tag(IController.TAG).i("onCapabilitiesChanged network= $network, networkCapabilities = $networkCapabilities")
            synchronized(weakNetMapper) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_WIFI)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    if (CsNetManager.isMobile2G()) {
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_2G)
                            }
                        }
                    } else if (CsNetManager.isMobile3G()) {
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_3G)
                            }
                        }
                    } else if (CsNetManager.isMobile4G()) {
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_4G)
                            }
                        }
                    } else if (CsNetManager.isMobile5G()) {
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_5G)
                            }
                        }
                    } else {
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_UNKNOWN)
                            }
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_BLUETOOTH)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_ETHERNET)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_VPN)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_WIFI_AWARE)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_LOWPAN)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_USB)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_USB)
                        }
                    }
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_THREAD)) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_THREAD)
                        }
                    }
                } else {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_UNKNOWN)
                        }
                    }
                }
                callback()
            }
        }

//        override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
//            super.onLinkPropertiesChanged(network, linkProperties)
//        }
    }
}