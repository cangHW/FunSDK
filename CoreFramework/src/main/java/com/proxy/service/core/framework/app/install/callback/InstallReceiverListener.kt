package com.proxy.service.core.framework.app.install.callback

import android.content.Context
import com.proxy.service.core.framework.app.install.status.InstallStatusEnum

/**
 * 接收消息回调
 *
 * @author: cangHX
 * @data: 2024/12/2 15:23
 * @desc:
 */
interface InstallReceiverListener {

    /**
     * 接收到消息
     */
    fun onReceive(context: Context, installStatusEnum: InstallStatusEnum, packageName: String)

}