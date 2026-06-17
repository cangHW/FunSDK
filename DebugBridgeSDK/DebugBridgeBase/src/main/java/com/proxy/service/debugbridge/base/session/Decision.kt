package com.proxy.service.debugbridge.base.session

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc: 调试会话的人工决策结果
 */
sealed class Decision {

    /**
     * 放行，返回原始结果
     * */
    object PassThrough : Decision()

    /**
     * 替换返回值
     * */
    data class Replace(
        val body: String,
        val code: Int = 200,
        val message: String = "OK"
    ) : Decision()

    /**
     * 中止请求，向上抛出异常
     * */
    data class Abort(
        val reason: String = "Aborted by debugger"
    ) : Decision()

    /**
     * 超时自动放行
     * */
    object TimeoutPassThrough : Decision()
}
