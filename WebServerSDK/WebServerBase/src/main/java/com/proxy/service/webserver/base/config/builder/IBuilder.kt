package com.proxy.service.webserver.base.config.builder

import androidx.annotation.IntRange
import com.proxy.service.webserver.base.config.WebServerConfig
import com.proxy.service.webserver.base.constants.Constants

/**
 * @author: cangHX
 * @date: 2026/6/15 17:14
 * @desc:
 */
interface IBuilder {

    /**
     * 设置 Web 服务监听端口，默认 [Constants.DEFAULT_PORT]
     *
     * @param port 端口号
     */
    fun setPort(@IntRange(from = Constants.MIN_PORT, to = Constants.MAX_PORT) port: Int): IBuilder

    /**
     * 构建配置对象
     */
    fun build(): WebServerConfig
}
