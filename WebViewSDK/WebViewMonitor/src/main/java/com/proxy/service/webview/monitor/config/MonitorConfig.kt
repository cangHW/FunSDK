package com.proxy.service.webview.monitor.config

import com.proxy.service.webview.base.web.callback.ValueCallback

/**
 * @author: cangHX
 * @data: 2026/1/23 12:55
 * @desc:
 */
class MonitorConfig private constructor(
    private val builder: IMonitorBuilderGet
) : IMonitorBuilderGet {

    override fun isLogCookieEnable(): Boolean {
        return builder.isLogCookieEnable()
    }

    override fun getLogCookieCallback(): ValueCallback<String>? {
        return builder.getLogCookieCallback()
    }

    override fun isLogAjaxRequestEnable(): Boolean {
        return builder.isLogAjaxRequestEnable()
    }

    override fun getLogAjaxRequestCallback(): ValueCallback<String>? {
        return builder.getLogAjaxRequestCallback()
    }

    companion object {
        fun builder(): IMonitorBuilder {
            return Builder()
        }
    }

    class Builder : IMonitorBuilder, IMonitorBuilderGet {

        private var logCookieEnable = false
        private var logCookieCallback: ValueCallback<String>? = null

        private var logAjaxRequestEnable = false
        private var logAjaxRequestCallback: ValueCallback<String>? = null

        override fun enableLogCookie(
            enable: Boolean,
            callback: ValueCallback<String>?
        ): IMonitorBuilder {
            this.logCookieEnable = enable
            this.logCookieCallback = callback
            return this
        }

        override fun enableLogAjaxRequest(
            enable: Boolean,
            callback: ValueCallback<String>?
        ): IMonitorBuilder {
            this.logAjaxRequestEnable = enable
            this.logAjaxRequestCallback = callback
            return this
        }

        override fun build(): MonitorConfig {
            return MonitorConfig(this)
        }

        override fun isLogCookieEnable(): Boolean {
            return logCookieEnable
        }

        override fun getLogCookieCallback(): ValueCallback<String>? {
            return logCookieCallback
        }

        override fun isLogAjaxRequestEnable(): Boolean {
            return logAjaxRequestEnable
        }

        override fun getLogAjaxRequestCallback(): ValueCallback<String>? {
            return logAjaxRequestCallback
        }
    }

}