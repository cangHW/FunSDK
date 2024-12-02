package com.proxy.service.core.framework.app.message.event.base

/**
 * @author: cangHX
 * @data: 2024/11/29 11:41
 * @desc:
 */
interface ISend {

    /**
     * 根据数据类型发送给对应监听
     * */
    fun sendEventValue(any: Any)

}