package com.proxy.service.core.framework.app.message.event.impl

import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseAlwaysActiveSend
import com.proxy.service.core.framework.data.log.CsLogger
import java.lang.ref.WeakReference

/**
 * @author: cangHX
 * @date: 2024/11/29 10:39
 * @desc:
 */
class WorkThreadAlwaysSendImpl(
    callbackRef: WeakReference<WorkThreadEventCallback>
) : BaseAlwaysActiveSend<WorkThreadEventCallback>(callbackRef) {

    override fun onActive() {
        handler?.clearAllTaskWithTag(tag)
        handler?.start(tag) {
            controller.forEachCache { value ->
                try {
                    if (controller.use(value)) {
                        callbackRef?.get()?.onWorkEvent(value)
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(EventConfig.TAG).e(throwable)
                }
            }
        }
    }
}