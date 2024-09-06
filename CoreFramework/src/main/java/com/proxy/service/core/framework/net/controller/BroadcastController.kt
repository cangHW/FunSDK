package com.proxy.service.core.framework.net.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import com.proxy.service.core.framework.context.CsContextManager
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.core.framework.net.NetType
import com.proxy.service.core.framework.net.CsNetUtils
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/7/23 10:53
 * @desc:
 */
class BroadcastController(
    private val weakNetMapper: WeakHashMap<CsNetUtils.NetConnectChangedListener, Any>,
    private val callback: () -> Unit
) : IController {

    private val netIntentFilter = IntentFilter()

    init {
        netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        CsLogger.i("run BroadcastController")
    }

    override fun start() {
        CsLogger.i("start")
        CsContextManager.getApplication().registerReceiver(mReceiver, netIntentFilter)
    }

    override fun stop() {
        CsLogger.i("stop")
        CsContextManager.getApplication().unregisterReceiver(mReceiver)
    }


    private val netHas = 0
    private val netNo = 1

    @Volatile
    private var isHasNet = -1

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            CsLogger.i("onReceive")
            val check = CsContextManager.getApplication()
                .checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
            if (check != PackageManager.PERMISSION_GRANTED) {
                synchronized(weakNetMapper) {
                    weakNetMapper.keys.forEach {
                        IController.runUiThread {
                            it.onNetChanged(NetType.TRANSPORT_UNKNOWN)
                        }
                    }
                    callback()
                }
                return
            }

            if (CsNetUtils.isAvailable()) {
                synchronized(weakNetMapper) {
                    if (isHasNet != netHas) {
                        isHasNet = netHas
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetConnected()
                            }
                        }
                    }
                    weakNetMapper.keys.forEach {
                        if (CsNetUtils.isWifi()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_WIFI)
                            }
                        } else if (CsNetUtils.isMobile2G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_2G)
                            }
                        } else if (CsNetUtils.isMobile3G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_3G)
                            }
                        } else if (CsNetUtils.isMobile4G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_4G)
                            }
                        } else if (CsNetUtils.isMobile5G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_5G)
                            }
                        } else {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_UNKNOWN)
                            }
                        }
                    }
                    callback()
                }
            } else {
                if (isHasNet != netNo) {
                    isHasNet = netNo
                    synchronized(weakNetMapper) {
                        weakNetMapper.keys.forEach {
                            IController.runUiThread {
                                it.onNetDisConnected()
                            }
                        }
                        callback()
                    }
                }
            }
        }
    }

}