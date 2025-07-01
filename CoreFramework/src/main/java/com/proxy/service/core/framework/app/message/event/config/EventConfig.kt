package com.proxy.service.core.framework.app.message.event.config

import com.proxy.service.core.constants.CoreConfig

/**
 * @author: cangHX
 * @data: 2024/11/29 11:13
 * @desc:
 */
object EventConfig {

    const val TAG = "${CoreConfig.TAG}Event"

    const val THREAD_EVENT = "thread-event"
    const val THREAD_EVENT_ALWAYS_ACTIVE = "thread-event-always-active"
    const val THREAD_EVENT_LIFECYCLE_ACTIVE = "thread-event-lifecycle-active"

}