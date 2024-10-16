package com.proxy.service.webview.info.web.script

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.webview.info.config.Config
import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import java.lang.reflect.Method

/**
 * @author: cangHX
 * @data: 2024/8/1 16:08
 * @desc:
 */
class MethodInterceptor(private val method: Method, private val obj: Any) {

    private val tag = "${Config.LOG_TAG_START}Interceptor"

    @RuntimeType
    fun invoke(@AllArguments params: Array<Any>) {
        try {
            method.isAccessible = true
            method.invoke(obj, *params)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable, "${method.name} - 参数错误: ${params.contentToString()}")
        }
    }

}