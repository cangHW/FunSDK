package com.proxy.service.core.framework.system.net.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.NetType
import com.proxy.service.core.framework.system.net.CsNetManager
import com.proxy.service.core.framework.system.net.callback.NetConnectChangedListener
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/7/23 10:53
 * @desc:
 */
class BroadcastController(
    private val weakNetMapper: WeakHashMap<NetConnectChangedListener, Any>,
    private val callback: () -> Unit
) : IController {

    private val netIntentFilter = IntentFilter()

    init {
        netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        CsLogger.tag(IController.TAG).i("run BroadcastController")
    }

    override fun start() {
        CsLogger.tag(IController.TAG).i("start")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CsContextManager.getApplication()
                .registerReceiver(mReceiver, netIntentFilter, Context.RECEIVER_EXPORTED)
        } else {
            CsContextManager.getApplication().registerReceiver(mReceiver, netIntentFilter)
        }
    }

    override fun stop() {
        CsLogger.tag(IController.TAG).i("stop")
        CsContextManager.getApplication().unregisterReceiver(mReceiver)
    }


    private val netHas = 0
    private val netNo = 1

    @Volatile
    private var isHasNet = -1

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            CsLogger.tag(IController.TAG).i("onReceive")
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

            if (CsNetManager.isAvailable()) {
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
                        if (CsNetManager.isWifi()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_WIFI)
                            }
                        } else if (CsNetManager.isMobile2G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_2G)
                            }
                        } else if (CsNetManager.isMobile3G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_3G)
                            }
                        } else if (CsNetManager.isMobile4G()) {
                            IController.runUiThread {
                                it.onNetChanged(NetType.TRANSPORT_CELLULAR_4G)
                            }
                        } else if (CsNetManager.isMobile5G()) {
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