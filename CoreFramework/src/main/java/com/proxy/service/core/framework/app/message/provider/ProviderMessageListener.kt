package com.proxy.service.core.framework.app.message.provider

import android.content.Context
import android.os.Bundle

/**
 * 接收消息回调
 *
 * @author: cangHX
 * @data: 2024/12/2 15:20
 * @desc:
 */
interface ProviderMessageListener {

    /**
     * 接收到消息
     */
    fun onReceive(context: Context?, fromPkg: String, arg: String?, extras: Bundle?): Bundle?

}