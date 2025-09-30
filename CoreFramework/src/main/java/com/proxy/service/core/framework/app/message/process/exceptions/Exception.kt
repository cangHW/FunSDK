package com.proxy.service.core.framework.app.message.process.exceptions

/**
 * 功能不支持
 *
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

/**
 * 服务端异常
 *
 * @author: cangHX
 * @data: 2025/9/18 11:46
 * @desc:
 */
class SeverException(
    private val code: Int,
    message: String
) : Throwable(message) {

    override fun toString(): String {
        return "SeverException(code=$code, message=$message, cause=${cause?.message})"
    }

}


/**
 * 超时
 *
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


/**
 * 未安装
 *
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