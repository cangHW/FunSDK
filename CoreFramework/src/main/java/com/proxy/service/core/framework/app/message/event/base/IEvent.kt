package com.proxy.service.core.framework.app.message.event.base

/**
 * @author: cangHX
 * @data: 2024/11/28 20:24
 * @desc:
 */
interface IEvent {
    /**
     * 设置接收消息的类型
     *
     * @return 支持的消息类型集合
     */
    fun getSupportTypes(): Set<Class<*>>

    /**
     * 是否只接收最新消息, 如果是, 则在消息未达到接收时机时, 新消息会覆盖旧消息
     *
     * @since 需要配合 LifecycleOwner 使用, 如果未设置 LifecycleOwner, 则该方法不生效
     * */
    fun shouldReceiveOnlyLatestMessage(): Boolean {
        return false
    }
}