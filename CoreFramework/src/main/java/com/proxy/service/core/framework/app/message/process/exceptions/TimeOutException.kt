package com.proxy.service.core.framework.app.message.process.exceptions

/**
 * @author: cangHX
 * @data: 2025/9/18 11:46
 * @desc:
 */
class TimeOutException(
    private val code: Int,
    message: String
) : Throwable(message) {

    override fun toString(): String {
        return "TimeOutException(code=$code, message=$message)"
    }

}