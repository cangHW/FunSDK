package com.proxy.service.core.framework.app.message.process.exceptions

/**
 * @author: cangHX
 * @data: 2025/9/23 09:44
 * @desc:
 */
class NotSupportException(
    private val code: Int,
    message: String
) : Throwable(message) {

    override fun toString(): String {
        return "NotSupportException(code=$code, message=$message)"
    }

}