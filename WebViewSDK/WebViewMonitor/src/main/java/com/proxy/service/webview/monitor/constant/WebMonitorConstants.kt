package com.proxy.service.webview.monitor.constant

import com.proxy.service.webview.base.constants.WebViewConstants

/**
 * @author: cangHX
 * @date: 2026/1/23 13:59
 * @desc:
 */
object WebMonitorConstants {

    const val TAG = "${WebViewConstants.LOG_TAG_START}Monitor_"

    const val WEB_MONITOR_LOG_BRIDGE_NAME_SPACE = "CsWebMonitor"

    const val ENABLE_LOG_COMMON = true

    const val ENABLE_LOG_COOKIE = false
    const val ENABLE_LOG_REQUEST = false
    const val ENABLE_LOG_LOAD_PAGE_TIME = true
    const val ENABLE_LOG_LOAD_PAGE_RESOURCE_TIME = false

    const val MAX_URL_LOG_LENGTH = 200

    const val REQUEST_START_ENABLE_TOAST = true
    const val REQUEST_START_MAX_KEEP_NUM = 50
    const val REQUEST_START_MIN_NUM_FOR_CHECK = 2
    const val REQUEST_START_REPETITION_MIN_COUNT = 2
    const val REQUEST_START_REPETITION_WINDOW_MS: Long = 1000
    const val REQUEST_START_STORM_MIN_COUNT = 8
    const val REQUEST_START_STORM_WINDOW_MS: Long = 3000


}