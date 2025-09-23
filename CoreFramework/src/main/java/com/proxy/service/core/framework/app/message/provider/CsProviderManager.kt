package com.proxy.service.core.framework.app.message.provider

import android.os.Bundle
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * ContentProvider 相关操作工具，只能接收到当前类发送的消息。
 *
 * @author: cangHX
 * @data: 2025/9/22 10:43
 * @desc:
 */
object CsProviderManager {

    private const val METHOD_PREFIX = "app_"

    /**
     * 添加消息回调
     * */
    fun addReceiverListener(method: String, listener: ProviderMessageListener) {
        ContentProviderImpl.addReceiverListener(checkMethod(method), listener)
    }

    /**
     * 移除消息回调
     * */
    fun removeReceiverListener(method: String) {
        ContentProviderImpl.removeReceiverListener(checkMethod(method))
    }

    /**
     * 发送消息, 需要配置目标应用可见, 配置方式参考 [CsInstallUtils.isInstallApp]
     *
     * @param toPkg                 接收方包名
     * @param method                接收方-目标方法
     * @param arg                   目标方法参数，可为空
     * @param extras                额外参数，可为空
     *
     * @return 返回数据
     * */
    fun sendMessage(toPkg: String, method: String, arg: String?, extras: Bundle?): Bundle? {
        if (!CsInstallUtils.isInstallApp(toPkg)) {
            CsLogger.tag(ContentProviderImpl.TAG)
                .e("The target app is not installed. package: $toPkg")
            return null
        }

        val realMethodName = checkMethod(method)
        CsLogger.tag(ContentProviderImpl.TAG)
            .d("send: toPkg = $toPkg, method = $realMethodName, arg = $arg, extras = $extras")
        return ContentProviderImpl.sendMessage(toPkg, realMethodName, arg, extras)
    }

    private fun checkMethod(method: String): String {
        return if (method.startsWith(METHOD_PREFIX)) {
            method
        } else {
            "$METHOD_PREFIX$method"
        }
    }

}