package com.proxy.service.core.framework.app.message.event.impl

import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.base.BaseAlwaysActiveSend
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.security.md5.CsMd5Utils

/**
 * @author: cangHX
 * @data: 2024/11/29 10:39
 * @desc:
 */
class WorkThreadAlwaysSendImpl(
    callback: WorkThreadEventCallback
) : BaseAlwaysActiveSend<WorkThreadEventCallback>(callback) {

    override fun onActive() {
        handler?.clearAllTaskWithTag(tag)
        handler?.start(tag) {
            controller?.forEachCache { value ->
                try {
                    if (controller.use(value)) {
                        callback?.onWorkEvent(value)
                    }
                } catch (throwable: Throwable) {
                    CsLogger.tag(EventConfig.TAG).e(throwable)
                }
            }
        }
    }
}