package com.proxy.service.core.framework.app.message.process.callback

import com.proxy.service.core.framework.app.message.process.bean.ShareMessage

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
     * */
    open fun onProgress(message: ShareMessage) {}

    /**
     * 成功回调
     * */
    abstract fun onSuccess(message: ShareMessage)

    /**
     * 失败回调
     * */
    abstract fun onFailed(code: Int, throwable: Throwable)

}