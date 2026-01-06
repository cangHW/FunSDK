package com.proxy.service.funsdk.webview

import android.webkit.JavascriptInterface
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/1/6 21:25
 * @desc:
 */
class WebBridge {

    @JavascriptInterface
    fun close() {
        CsLogger.tag("qqqqqq").i("close")
    }

}