package com.proxy.service.webview.info.view.group

import android.view.ViewGroup
import com.proxy.service.webview.info.view.WebViewImpl

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object ViewGroupFactory : IFactory<ViewGroup> {
    override fun loadWebView(viewGroup: ViewGroup, webView: WebViewImpl) {
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        viewGroup.addView(webView, params)
    }
}