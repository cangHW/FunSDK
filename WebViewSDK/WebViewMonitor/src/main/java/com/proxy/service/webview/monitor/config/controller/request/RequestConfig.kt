package com.proxy.service.webview.monitor.config.controller.request

import com.proxy.service.webview.monitor.config.controller.base.Config
import com.proxy.service.webview.monitor.constant.WebMonitorConstants

/**
 * @author: cangHX
 * @date: 2026/6/8 14:28
 * @desc:
 */
class RequestConfig(
    private val requestConfigGet: IRequestConfigGet
) : IRequestConfigGet by requestConfigGet {

    companion object {
        fun builder(): IRequestConfig {
            return Builder()
        }
    }

    private class Builder : Config<IRequestConfig>(), IRequestConfig, IRequestConfigGet {

        private var repetitionMinCount = WebMonitorConstants.REQUEST_START_REPETITION_MIN_COUNT
        private var repetitionMaxTime = WebMonitorConstants.REQUEST_START_REPETITION_WINDOW_MS
        private var repetitionShowToast = WebMonitorConstants.REQUEST_START_ENABLE_TOAST

        private var stormMinCount = WebMonitorConstants.REQUEST_START_STORM_MIN_COUNT
        private var stormMaxTime = WebMonitorConstants.REQUEST_START_STORM_WINDOW_MS
        private var stormShowToast = WebMonitorConstants.REQUEST_START_ENABLE_TOAST

        override fun getInstance(): IRequestConfig {
            return this
        }

        override fun setRepetition(
            minCount: Int,
            maxTime: Long,
            showToast: Boolean
        ): IRequestConfig {
            this.repetitionMinCount = minCount
            if (maxTime > 0) {
                this.repetitionMaxTime = maxTime
            }
            this.repetitionShowToast = showToast
            return getInstance()
        }

        override fun setStorm(minCount: Int, maxTime: Long, showToast: Boolean): IRequestConfig {
            this.stormMinCount = minCount
            if (maxTime > 0) {
                this.stormMaxTime = maxTime
            }
            this.stormShowToast = showToast
            return getInstance()
        }

        override fun build(): RequestConfig {
            return RequestConfig(this)
        }

        override fun getRepetitionMinCount(): Int {
            return repetitionMinCount
        }

        override fun getRepetitionMaxTime(): Long {
            return repetitionMaxTime
        }

        override fun getRepetitionToastEnable(): Boolean {
            return repetitionShowToast
        }

        override fun getStormMinCount(): Int {
            return stormMinCount
        }

        override fun getStormMaxTime(): Long {
            return stormMaxTime
        }

        override fun getStormToastEnable(): Boolean {
            return stormShowToast
        }

    }

}