package com.proxy.service.core.framework.system.net.callback

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.NetType
import com.proxy.service.core.framework.system.net.controller.AbstractController

/**
 * 网络连接状态变化监听
 *
 * @author: cangHX
 * @date: 2024/12/2 15:41
 * @desc:
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
        CsLogger.tag(AbstractController.TAG).d("ScNetType: $type")
    }

    /**
     * 网络断开连接
     */
    fun onNetDisConnected()

}