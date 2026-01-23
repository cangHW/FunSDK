package com.proxy.service.core.framework.app.message.broadcast.callback

import android.os.Bundle

/**
 * @author: cangHX
 * @data: 2026/1/19 11:31
 * @desc:
 */
interface OrderBroadcastCallback {

    /**
     * 有序广播结束回调
     *
     * @param isReceiverAvailable   是否存在接收方, 如果存在一个或多个则该值为 true
     * @param bundle                返回值
     * */
    fun onFinal(isReceiverAvailable: Boolean, bundle: Bundle)

}