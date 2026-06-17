package com.proxy.service.webserver.base.plugin

/**
 * 业务数据提交至插件后的处理结果
 *
 * 由 [IWebServerPlugin.handlePluginData] 返回，经 [com.proxy.service.webserver.base.WebServerService.submitPluginData] 传递给调用方
 */
sealed class PluginDataResponse {

    /**
     * 处理成功
     */
    object SUCCESS : PluginDataResponse()

    /**
     * 处理失败
     */
    class FAILED(val message: String) : PluginDataResponse()

    /**
     * 自定义处理结果
     */
    class Custom(
        val code: Int,
        val type: Int,
        val payload: String
    ) : PluginDataResponse()
}
