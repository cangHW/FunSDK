package com.proxy.service.core.framework.app.message.process.bean

/**
 * @author: cangHX
 * @data: 2025/9/29 16:15
 * @desc:
 */
enum class MessageType {

    /**
     * 未知类型
     * */
    UNKNOWN,

    /**
     * 同步请求
     * */
    REQUEST_SYNC,

    /**
     * 异步请求
     * */
    REQUEST_ASYNC,

    /**
     * 异步请求成功, 等待结果
     * */
    RESPONSE_WAITING,

    /**
     * 异步请求成功, 返回进度
     * */
    RESPONSE_PROGRESS,

    /**
     * 请求成功, 返回结果
     * */
    RESPONSE_FINISH,

    /**
     * 请求失败
     * */
    RESPONSE_ERROR;


    companion object {
        fun valueOf(name: String): MessageType {
            return when (name) {
                REQUEST_SYNC.name -> {
                    REQUEST_SYNC
                }

                REQUEST_ASYNC.name -> {
                    REQUEST_ASYNC
                }

                RESPONSE_WAITING.name -> {
                    RESPONSE_WAITING
                }

                RESPONSE_PROGRESS.name -> {
                    RESPONSE_PROGRESS
                }

                RESPONSE_FINISH.name -> {
                    RESPONSE_FINISH
                }

                RESPONSE_ERROR.name -> {
                    RESPONSE_ERROR
                }

                else -> {
                    UNKNOWN
                }
            }
        }
    }

}