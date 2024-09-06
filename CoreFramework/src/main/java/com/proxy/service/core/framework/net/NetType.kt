package com.proxy.service.core.framework.net

/**
 * @author: cangHX
 * @data: 2024/7/22 20:27
 * @desc:
 */
enum class NetType {

    /**
     * 未知，状态变化，但是无法判断是否有网
     * */
    TRANSPORT_UNKNOWN,

    /**
     * 表示蜂窝移动数据传输，未知模式
     * */
    TRANSPORT_CELLULAR_UNKNOWN,

    /**
     * 表示蜂窝移动数据传输，2G
     * */
    TRANSPORT_CELLULAR_2G,

    /**
     * 表示蜂窝移动数据传输，3G
     * */
    TRANSPORT_CELLULAR_3G,

    /**
     * 表示蜂窝移动数据传输，4G
     * */
    TRANSPORT_CELLULAR_4G,

    /**
     * 表示蜂窝移动数据传输，5G
     * */
    TRANSPORT_CELLULAR_5G,

    /**
     * Wi-Fi无线局域网传输
     * */
    TRANSPORT_WIFI,

    /**
     * 蓝牙传输，用于短距离无线通信
     * */
    TRANSPORT_BLUETOOTH,

    /**
     * 以太网有线连接，通常用于局域网中的有线连接
     * */
    TRANSPORT_ETHERNET,

    /**
     * 虚拟专用网络（VPN）传输，通过加密隧道进行安全的数据传输
     * */
    TRANSPORT_VPN,

    /**
     * Wi-Fi Aware传输，这是一种设备间直接发现和通信技术，不需要互联网接入点
     * */
    TRANSPORT_WIFI_AWARE,

    /**
     * 低功耗无线个人区域网络（Low-Power Wireless Personal Area Network），例如基于IEEE 802.15.4标准的网络
     * */
    TRANSPORT_LOWPAN,

    /**
     * 通过USB接口进行的数据传输
     * */
    TRANSPORT_USB,

    /**
     * Thread 网络传输，Thread 是一种IPv6低功耗无线协议，主要应用于智能家居领域
     * */
    TRANSPORT_THREAD;

}