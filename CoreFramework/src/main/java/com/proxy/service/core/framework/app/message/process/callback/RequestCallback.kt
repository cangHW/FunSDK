package com.proxy.service.core.framework.app.message.process.callback

/**
 * @author: cangHX
 * @data: 2025/9/17 20:38
 * @desc:
 */
abstract class RequestCallback {

    companion object {

        /**
         * 目标应用未安装
         * */
        const val ERROR_CODE_UNINSTALL = -1

        /**
         * 超时
         * */
        const val ERROR_CODE_TIME_OUT = -2

        /**
         * 目标功能不支持
         * */
        const val ERROR_METHOD_NOT_SUPPORT = -3

        /**
         * 调用失败, 目标方异常
         * */
        const val ERROR_CODE_SEVER_ERROR = -4
    }

    /**
     * 进度回调
     *
     * @param version   版本
     * @param time      进度消息开始返回时间, 可用于性能统计, 13 位时间戳
     * @param method    功能名称
     * @param content   消息内容
     * */
    open fun onProgress(version: String, time: Long, method: String, content: String) {}

    /**
     * 成功回调
     *
     * @param version   版本
     * @param time      成功消息开始返回时间, 可用于性能统计, 13 位时间戳
     * @param method    功能名称
     * @param content   消息内容
     * */
    abstract fun onSuccess(version: String, time: Long, method: String, content: String)

    /**
     * 失败回调
     *
     * @param code      错误码
     * @param throwable 错误信息
     * */
    abstract fun onFailed(code: Int, throwable: Throwable)

}