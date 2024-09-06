package com.proxy.service.webview.info.web.callback

import android.webkit.ValueCallback


/**
 * @author: cangHX
 * @data: 2024/8/10 16:46
 * @desc:
 */
class ValueCallbackImpl(private val callback: com.proxy.service.webview.base.web.callback.ValueCallback<String?>?) :
    ValueCallback<String> {
    override fun onReceiveValue(p0: String?) {
        callback?.onReceiveValue(p0)
    }
}