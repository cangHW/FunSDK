package com.proxy.service.core.framework.app.worker.enums

import androidx.work.NetworkType

/**
 * @author: cangHX
 * @data: 2024/1/3 17:06
 * @desc:
 */
enum class NetworkTypeEnum(val type: NetworkType){
    /**
     * 不需要网络
     */
    NOT_REQUIRED(NetworkType.NOT_REQUIRED),

    /**
     * 需要网络连接
     */
    CONNECTED(NetworkType.CONNECTED),

    /**
     * 不计费的网络连接，如：WI-FI 等
     */
    UNMETERED(NetworkType.UNMETERED),

    /**
     * 非漫游的网络连接
     */
    NOT_ROAMING(NetworkType.NOT_ROAMING),

    /**
     * 计费的网络连接，如：3G、4G、5G 等
     */
    METERED(NetworkType.METERED)
}