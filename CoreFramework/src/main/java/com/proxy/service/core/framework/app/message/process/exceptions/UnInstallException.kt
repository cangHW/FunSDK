package com.proxy.service.core.framework.app.message.process.exceptions

/**
 * @author: cangHX
 * @data: 2025/9/18 11:44
 * @desc:
 */
class UnInstallException(
    private val code: Int,
    message: String
) : Throwable(message) {

    override fun toString(): String {
        return "UnInstallException(code=$code, message=$message)"
    }
}