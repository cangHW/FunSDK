package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.monitor.callback.MonitorCallback
import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @date: 2026/1/23 12:55
 * @desc:
 */
class MonitorConfig private constructor(
    private val builder: IMonitorBuilderGet
) : IMonitorBuilderGet {

    override fun isLogCookieEnable(): Boolean {
        return builder.isLogCookieEnable()
    }

    override fun getLogCookieCallback(): MonitorCallback? {
        return builder.getLogCookieCallback()
    }

    override fun isLogRequestEnable(): Boolean {
        return builder.isLogRequestEnable()
    }

    override fun getLogRequestCallback(): MonitorCallback? {
        return builder.getLogRequestCallback()
    }

    override fun isLogLoadPageTimeEnable(): Boolean {
        return builder.isLogLoadPageTimeEnable()
    }

    override fun getLogLoadPageTimeCallback(): MonitorCallback? {
        return builder.getLogLoadPageTimeCallback()
    }

    override fun isLogLoadPageResourceTimeEnable(): Boolean {
        return builder.isLogLoadPageResourceTimeEnable()
    }

    override fun getLogLoadPageResourceTimeCallback(): MonitorCallback? {
        return builder.getLogLoadPageResourceTimeCallback()
    }

    companion object {
        fun builder(): IMonitorBuilder {
            return Builder()
        }
    }

    class Builder : IMonitorBuilder, IMonitorBuilderGet {

        private var logCookieEnable = WebMonitorConstants.ENABLE_LOG_COOKIE
        private var logCookieCallback: MonitorCallback? = null

        private var logRequestEnable = WebMonitorConstants.ENABLE_LOG_REQUEST
        private var logRequestCallback: MonitorCallback? = null

        private var logLoadPageTimeEnable = WebMonitorConstants.ENABLE_LOG_LOAD_PAGE_TIME
        private var logLoadPageTimeCallback: MonitorCallback? = null
        private var logLoadPageResourceTimeEnable = WebMonitorConstants.ENABLE_LOG_LOAD_PAGE_RESOURCE_TIME
        private var logLoadPageResourceTimeCallback: MonitorCallback? = null

        override fun enableLogCookie(
            enable: Boolean,
            callback: MonitorCallback?
        ): IMonitorBuilder {
            this.logCookieEnable = enable
            this.logCookieCallback = callback
            return this
        }

        override fun enableLogRequest(
            enable: Boolean,
            callback: MonitorCallback?
        ): IMonitorBuilder {
            this.logRequestEnable = enable
            this.logRequestCallback = callback
            return this
        }

        override fun enableLogLoadPageTime(
            enable: Boolean,
            callback: MonitorCallback?
        ): IMonitorBuilder {
            this.logLoadPageTimeEnable = enable
            this.logLoadPageTimeCallback = callback
            return this
        }

        override fun enableLogLoadPageResourceTime(
            enable: Boolean,
            callback: MonitorCallback?
        ): IMonitorBuilder {
            this.logLoadPageResourceTimeEnable = enable
            this.logLoadPageResourceTimeCallback = callback
            return this
        }

        override fun build(): MonitorConfig {
            return MonitorConfig(this)
        }

        override fun isLogCookieEnable(): Boolean {
            return logCookieEnable
        }

        override fun getLogCookieCallback(): MonitorCallback? {
            return logCookieCallback
        }

        override fun isLogRequestEnable(): Boolean {
            return logRequestEnable
        }

        override fun getLogRequestCallback(): MonitorCallback? {
            return logRequestCallback
        }

        override fun isLogLoadPageTimeEnable(): Boolean {
            return logLoadPageTimeEnable
        }

        override fun getLogLoadPageTimeCallback(): MonitorCallback? {
            return logLoadPageTimeCallback
        }

        override fun isLogLoadPageResourceTimeEnable(): Boolean {
            return logLoadPageResourceTimeEnable
        }

        override fun getLogLoadPageResourceTimeCallback(): MonitorCallback? {
            return logLoadPageResourceTimeCallback
        }
    }

}