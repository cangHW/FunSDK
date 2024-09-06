package com.proxy.service.webview.info.view.group

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.proxy.service.webview.info.view.WebViewImpl

/**
 * @author: cangHX
 * @data: 2024/6/5 07:59
 * @desc:
 */
interface IFactory<T : ViewGroup> {

    fun loadWebView(viewGroup: T, webView: WebViewImpl)

    companion object {
        fun of(viewGroup: ViewGroup, webView: WebViewImpl) {
            when (viewGroup) {
                is LinearLayout -> {
                    LinearLayoutFactory.loadWebView(viewGroup, webView)
                }

                is RelativeLayout -> {
                    RelativeLayoutFactory.loadWebView(viewGroup, webView)
                }

                is FrameLayout -> {
                    FrameLayoutFactory.loadWebView(viewGroup, webView)
                }

                is ConstraintLayout -> {
                    ConstraintLayoutFactory.loadWebView(viewGroup, webView)
                }

                else -> {
                    ViewGroupFactory.loadWebView(viewGroup, webView)
                }
            }
        }
    }

}