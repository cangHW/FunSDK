package com.proxy.service.core.framework.net

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager
import com.proxy.service.core.framework.context.CsContextManager
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.core.framework.net.controller.BroadcastController
import com.proxy.service.core.framework.net.controller.NetworkController
import com.proxy.service.core.framework.net.controller.IController
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * android.Manifest.permission.READ_PHONE_STATE
 * android.Manifest.permission.ACCESS_NETWORK_STATE
 *
 * @author: cangHX
 * @data: 2024/4/28 15:34
 * @desc:
 */
object CsNetUtils {

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
    private fun getNetworkCapabilities(connectivityManager: ConnectivityManager? = null): NetworkCapabilities? {
        val check = CsContextManager.getApplication()
            .checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
        if (check != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val manager = connectivityManager ?: getConnectivityManager()
        return manager?.activeNetwork?.let {
            manager.getNetworkCapabilities(it)
        }
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

    /**
     * 网络连接状态变化监听
     */
    interface NetConnectChangedListener {
        /**
         * 网络连接
         */
        fun onNetConnected()

        /**
         * 网络连接变化
         * */
        fun onNetChanged(type: NetType) {
            CsLogger.d("ScNetType: $type")
        }

        /**
         * 网络断开连接
         */
        fun onNetDisConnected()
    }

    private val any = Any()
    private val isReceiverRun = AtomicBoolean(false)
    private val weakNetMapper = WeakHashMap<NetConnectChangedListener, Any>()
    private var controller: IController? = null

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
    fun removeWeakNetConnectChangedListener(listener: NetConnectChangedListener) {
        synchronized(weakNetMapper) {
            weakNetMapper.remove(listener)
            checkWeakNetMapperState()
        }
    }

    private fun checkWeakNetMapperState() {
        if (weakNetMapper.size == 0) {
            controller?.stop()
            controller = null
            isReceiverRun.set(false)
        }
    }
}