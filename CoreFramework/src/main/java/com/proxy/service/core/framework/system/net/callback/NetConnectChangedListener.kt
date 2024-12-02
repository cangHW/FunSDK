package com.proxy.service.core.framework.system.net.callback

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.NetType
import com.proxy.service.core.framework.system.net.controller.IController

/**
 * 网络连接状态变化监听
 *
 * @author: cangHX
 * @data: 2024/12/2 15:41
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
        CsLogger.tag(IController.TAG).d("ScNetType: $type")
    }

    /**
     * 网络断开连接
     */
    fun onNetDisConnected()

}