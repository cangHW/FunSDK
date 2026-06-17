package com.proxy.service.webserver.base.config

import com.proxy.service.webserver.base.config.builder.IBuilder
import com.proxy.service.webserver.base.config.builder.IBuilderGet
import com.proxy.service.webserver.base.constants.Constants

/**
 * @author: cangHX
 * @date: 2026/6/15 17:17
 * @desc:
 */
class WebServerConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet by builder {

    companion object {

        /**
         * 创建配置构建器
         */
        fun builder(): IBuilder {
            return Builder()
        }
    }

    private class Builder : IBuilder, IBuilderGet {

        private var port: Int = Constants.DEFAULT_PORT

        override fun setPort(port: Int): IBuilder {
            if (port in Constants.MIN_PORT..Constants.MAX_PORT) {
                this.port = port
            }
            return this
        }

        override fun build(): WebServerConfig {
            return WebServerConfig(this)
        }

        override fun getPort(): Int {
            return port
        }
    }
}
