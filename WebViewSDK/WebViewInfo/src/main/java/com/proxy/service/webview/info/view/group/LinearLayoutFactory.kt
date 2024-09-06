package com.proxy.service.webview.info.view.group

import android.view.Gravity
import android.widget.LinearLayout
import com.proxy.service.webview.info.view.WebViewImpl

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object LinearLayoutFactory : IFactory<LinearLayout> {
    override fun loadWebView(viewGroup: LinearLayout, webView: WebViewImpl) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        viewGroup.addView(webView, params)
    }
}