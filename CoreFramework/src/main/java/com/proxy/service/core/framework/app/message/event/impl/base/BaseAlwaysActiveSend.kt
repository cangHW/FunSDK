package com.proxy.service.core.framework.app.message.event.impl.base

import com.proxy.service.core.framework.app.message.event.base.BaseSend
import com.proxy.service.core.framework.app.message.event.base.IEvent
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.service.task.CsTask
import java.lang.ref.WeakReference

/**
 * @author: cangHX
 * @date: 2024/12/2 11:38
 * @desc:
 */
abstract class BaseAlwaysActiveSend<T : IEvent>(
    protected var callbackRef: WeakReference<T>?,
) : BaseSend<T>(
    callbackRef?.get()?.getSupportTypes() ?: HashSet(),
    callbackRef?.get()?.shouldReceiveOnlyLatestMessage() ?: true
) {

    protected val handler = CsTask.launchTaskGroup(EventConfig.THREAD_EVENT_ALWAYS_ACTIVE)

    override fun checkState() {
        onActive()
    }

    /**
     * 活跃状态, 可以发送数据
     * */
    abstract fun onActive()
}