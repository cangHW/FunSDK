package com.proxy.service.core.framework.app.message.event.callback

import com.proxy.service.core.framework.app.message.event.base.IEvent

/**
 * @author: cangHX
 * @data: 2024/11/28 20:23
 * @desc:
 */
interface MainThreadEventCallback : IEvent {

    /**
     * 接收消息
     *
     * @param any   自定义消息体
     */
    fun onMainEvent(any: Any)

}