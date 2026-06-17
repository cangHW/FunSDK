package com.proxy.service.core.framework.system.net

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.system.net.callback.NetConnectChangedListener
import com.proxy.service.core.framework.system.net.controller.BroadcastController
import com.proxy.service.core.framework.system.net.controller.NetworkController
import com.proxy.service.core.framework.system.net.controller.AbstractController
import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 网络状态相关工具
 *
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *
 * @author: cangHX
 * @date: 2024/4/28 15:34
 * @desc:
 */
object CsNetManager {

    private var connectivityManager: ConnectivityManager? = null
    private var telephonyManager: TelephonyManager? = null

    private fun getConnectivityManager(): ConnectivityManager? {
        if (connectivityManager != null) {
            return connectivityManager
        }
        connectivityManager = CsContextManager.getApplication()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager
    }

    private fun getTelephonyManager(): TelephonyManager? {
        if (telephonyManager != null) {
            return telephonyManager
        }
        telephonyManager = CsContextManager.getApplication()
            .getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return telephonyManager
    }

    @SuppressLint("MissingPermission")
    private fun getTelephonyNetworkType(): Int? {
        val check = CsContextManager.getApplication()
            .checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
        if (check == PackageManager.PERMISSION_GRANTED) {
            return getTelephonyManager()?.networkType
        }
        return null
    }

    @SuppressLint("MissingPermission")
    private fun getActiveNetwork(connectivityManager: ConnectivityManager? = null): Network? {
        val check = CsContextManager.getApplication()
            .checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        if (check != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val manager = connectivityManager ?: getConnectivityManager()
        return manager?.activeNetwork
    }

    @SuppressLint("MissingPermission")
    private fun getNetworkCapabilities(connectivityManager: ConnectivityManager? = null): NetworkCapabilities? {
        val check = CsContextManager.getApplication()
            .checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        if (check != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val manager = connectivityManager ?: getConnectivityManager()
        val activeNetwork = getActiveNetwork(manager) ?: return null
        return manager?.getNetworkCapabilities(activeNetwork)
    }

    /**
     * 是否有网络
     * */
    fun isAvailable(): Boolean {
        return getNetworkCapabilities()?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    /**
     * 获取网络类型
     * */
    fun getNetType(): NetType {
        val networkCapabilities = getNetworkCapabilities() ?: return NetType.TRANSPORT_UNKNOWN
        if (isWifi(networkCapabilities)) {
            return NetType.TRANSPORT_WIFI
        } else if (isMobile(networkCapabilities)) {
            val networkType = getTelephonyNetworkType()
            if (isMobile2G(networkType)) {
                return NetType.TRANSPORT_CELLULAR_2G
            } else if (isMobile3G(networkType)) {
                return NetType.TRANSPORT_CELLULAR_3G
            } else if (isMobile4G(networkType)) {
                return NetType.TRANSPORT_CELLULAR_4G
            } else if (isMobile5G(networkType)) {
                return NetType.TRANSPORT_CELLULAR_5G
            }
            return NetType.TRANSPORT_CELLULAR_UNKNOWN
        }
        return NetType.TRANSPORT_UNKNOWN
    }

    /**
     * 判断当前是否是 wifi
     * */
    fun isWifi(networkCapabilities: NetworkCapabilities? = null): Boolean {
        return (networkCapabilities
            ?: getNetworkCapabilities())?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }

    /**
     * 判断当前是否是移动网络
     * */
    fun isMobile(networkCapabilities: NetworkCapabilities? = null): Boolean {
        return (networkCapabilities
            ?: getNetworkCapabilities())?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
    }

    private val mobile2GNetworkTypes = setOf(
        TelephonyManager.NETWORK_TYPE_GPRS,
        TelephonyManager.NETWORK_TYPE_CDMA,
        TelephonyManager.NETWORK_TYPE_EDGE,
        TelephonyManager.NETWORK_TYPE_1xRTT,
        TelephonyManager.NETWORK_TYPE_IDEN
    )

    /**
     * 判断当前是否是 2G
     * */
    fun isMobile2G(networkType: Int? = null): Boolean {
        return (networkType ?: getTelephonyNetworkType()) in mobile2GNetworkTypes
    }

    private val mobile3GNetworkTypes = setOf(
        TelephonyManager.NETWORK_TYPE_UMTS,
        TelephonyManager.NETWORK_TYPE_TD_SCDMA,
        TelephonyManager.NETWORK_TYPE_HSPA,
        TelephonyManager.NETWORK_TYPE_HSPAP,
        TelephonyManager.NETWORK_TYPE_HSDPA,
        TelephonyManager.NETWORK_TYPE_HSUPA,
        TelephonyManager.NETWORK_TYPE_EVDO_0,
        TelephonyManager.NETWORK_TYPE_EVDO_A,
        TelephonyManager.NETWORK_TYPE_EVDO_B,
        TelephonyManager.NETWORK_TYPE_EHRPD
    )

    /**
     * 判断当前是否是 3G
     * */
    fun isMobile3G(networkType: Int? = null): Boolean {
        return (networkType ?: getTelephonyNetworkType()) in mobile3GNetworkTypes
    }

    private val mobile4GNetworkTypes = setOf(
        TelephonyManager.NETWORK_TYPE_LTE,
        TelephonyManager.NETWORK_TYPE_IWLAN
    )

    /**
     * 判断当前是否是 4G
     * */
    fun isMobile4G(networkType: Int? = null): Boolean {
        return (networkType ?: getTelephonyNetworkType()) in mobile4GNetworkTypes
    }

    private val mobile5GNetworkTypes = setOf(
        TelephonyManager.NETWORK_TYPE_NR
    )

    /**
     * 判断当前是否是 5G
     * */
    fun isMobile5G(networkType: Int? = null): Boolean {
        return (networkType ?: getTelephonyNetworkType()) in mobile5GNetworkTypes
    }

    private val any = Any()
    private val isReceiverRun = AtomicBoolean(false)
    private val weakNetMapper = WeakHashMap<NetConnectChangedListener, Any>()
    private var controller: AbstractController? = null

    /**
     * 注册网络状态变化监听, 弱引用
     * */
    fun addWeakNetConnectChangedListener(listener: NetConnectChangedListener) {
        synchronized(weakNetMapper) {
            weakNetMapper[listener] = any
            if (isReceiverRun.compareAndSet(false, true)) {
                controller?.stop()
                val check = CsContextManager.getApplication()
                    .checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
                controller = if (check == PackageManager.PERMISSION_GRANTED) {
                    NetworkController(getConnectivityManager(), weakNetMapper) {
                        checkWeakNetMapperState()
                    }
                } else {
                    BroadcastController(weakNetMapper) {
                        checkWeakNetMapperState()
                    }
                }
                controller?.start()
            }
        }
    }

    /**
     * 移除网络状态变化监听
     * */
    fun removeNetConnectChangedListener(listener: NetConnectChangedListener) {
        synchronized(weakNetMapper) {
            weakNetMapper.remove(listener)
            checkWeakNetMapperState()
        }
    }

    private fun checkWeakNetMapperState() {
        synchronized(weakNetMapper) {
            if (weakNetMapper.size == 0) {
                controller?.stop()
                controller = null
                isReceiverRun.set(false)
            }
        }
    }

    private const val DEFAULT_IP_ADDRESS = "127.0.0.1"

    @SuppressLint("MissingPermission")
    fun getActiveNetworkIpAddress(): String {
        val connectivityManager = getConnectivityManager()
        val network = getActiveNetwork(connectivityManager)
        if (network != null) {
            val hostAddress = connectivityManager?.getLinkProperties(network)
                ?.linkAddresses
                ?.firstOrNull()
                ?.address
                ?.hostAddress

            if (hostAddress != null) {
                return hostAddress
            }
        }
        return DEFAULT_IP_ADDRESS
    }
}