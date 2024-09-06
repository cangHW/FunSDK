package com.proxy.service.webview.info.view.group

import android.widget.RelativeLayout
import com.proxy.service.webview.info.view.WebViewImpl

/**
 * @author: cangHX
 * @data: 2024/6/5 08:57
 * @desc:
 */
object RelativeLayoutFactory : IFactory<RelativeLayout> {
    override fun loadWebView(viewGroup: RelativeLayout, webView: WebViewImpl) {
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        viewGroup.addView(webView, params)
    }
}