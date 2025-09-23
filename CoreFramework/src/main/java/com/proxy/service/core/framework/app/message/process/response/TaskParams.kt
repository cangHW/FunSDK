package com.proxy.service.core.framework.app.message.process.response

import com.proxy.service.core.framework.app.message.process.callback.RequestCallback

/**
 * @author: cangHX
 * @data: 2025/9/18 10:07
 * @desc:
 */
data class TaskParams(
    val messageId: String,
    val timeout: Long,
    val callback: RequestCallback
)