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
 * @date: 2024/7/23 10:53
 * @desc:
 */
class BroadcastController(
    weakNetMapper: WeakHashMap<NetConnectChangedListener, Any>,
    callback: () -> Unit
) : AbstractController(weakNetMapper, callback) {

    private val netIntentFilter = IntentFilter()

    init {
        netIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        CsLogger.tag(TAG).i("run BroadcastController")
    }

    override fun start() {
        CsLogger.tag(TAG).i("start")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CsContextManager.getApplication()
                .registerReceiver(mReceiver, netIntentFilter, Context.RECEIVER_EXPORTED)
        } else {
            CsContextManager.getApplication().registerReceiver(mReceiver, netIntentFilter)
        }
    }

    override fun stop() {
        CsLogger.tag(TAG).i("stop")
        CsContextManager.getApplication().unregisterReceiver(mReceiver)
    }


    private val netHas = 0
    private val netNo = 1

    @Volatile
    private var isHasNet = -1

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            CsLogger.tag(TAG).i("onReceive")
            val check = CsContextManager.getApplication()
                .checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
            if (check != PackageManager.PERMISSION_GRANTED) {
                callNetChanged(NetType.TRANSPORT_UNKNOWN)
                return
            }

            if (CsNetManager.isAvailable()) {
                if (isHasNet != netHas) {
                    isHasNet = netHas
                    callNetConnected()
                }
                if (CsNetManager.isWifi()) {
                    callNetChanged(NetType.TRANSPORT_WIFI)
                } else if (CsNetManager.isMobile2G()) {
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
            } else {
                if (isHasNet != netNo) {
                    isHasNet = netNo
                    callNetDisConnected()
                }
            }
        }
    }

}