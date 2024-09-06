package com.proxy.service.webview.info.view.group

import android.view.Gravity
import android.widget.FrameLayout
import com.proxy.service.webview.info.view.WebViewImpl

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object FrameLayoutFactory : IFactory<FrameLayout> {
    override fun loadWebView(viewGroup: FrameLayout, webView: WebViewImpl) {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        viewGroup.addView(webView, params)
    }
}