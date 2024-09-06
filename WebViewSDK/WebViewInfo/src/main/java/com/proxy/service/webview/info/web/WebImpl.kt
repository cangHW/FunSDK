package com.proxy.service.webview.info.web

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.base.web.callback.ValueCallback
import com.proxy.service.webview.info.config.Config
import com.proxy.service.webview.info.view.WebViewImpl
import com.proxy.service.webview.info.view.group.IFactory
import com.proxy.service.webview.info.web.callback.ValueCallbackImpl
import com.proxy.service.webview.info.web.lifecycle.LifecycleObserverImpl

/**
 * @author: cangHX
 * @data: 2024/8/1 15:41
 * @desc:
 */
class WebImpl(private val webView: WebViewImpl, lifecycleOwner: LifecycleOwner?) : IWeb {

    private val tag = "${Config.LOG_TAG_START}IWeb"

    init {
        lifecycleOwner?.lifecycle?.addObserver(LifecycleObserverImpl(this))
    }

    override fun changeParentView(viewGroup: ViewGroup?) {
        CsLogger.tag(tag).d("changeParentView")
        if (viewGroup == null){
            return
        }
        webView.parent?.let {
            if (it is ViewGroup) {
                it.removeView(webView)
            }
        }
        IFactory.of(viewGroup, webView)
    }

    override fun setBackgroundColor(color: Int) {
        CsLogger.tag(tag).d("setBackgroundColor color = $color")
        webView.setBackgroundColor(color)
    }

    override fun setVisibility(visibility: Int) {
        CsLogger.tag(tag).d("setVisibility visibility = $visibility")
        webView.visibility = visibility
    }

    override fun setLayerType(layerType: Int, paint: Paint?) {
        CsLogger.tag(tag).d("setLayerType layerType = $layerType, paint = $paint")
        webView.setLayerType(layerType, paint)
    }

    override fun evaluateJavascript(script: String, resultCallback: ValueCallback<String?>?) {
        CsLogger.tag(tag).d("evaluateJavascript script = $script")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                webView.evaluateJavascript(script, ValueCallbackImpl(resultCallback))
                return ""
            }
        })?.start()
    }

    override fun loadUrl(url: String) {
        CsLogger.tag(tag).d("loadUrl")
        webView.loadUrl(url)
    }

    override fun reload() {
        CsLogger.tag(tag).d("reload")
        webView.reload()
    }

    override fun canGoBack(): Boolean {
        CsLogger.tag(tag).d("canGoBack")
        return webView.canGoBack()
    }

    override fun goBack() {
        CsLogger.tag(tag).d("goBack")
        webView.goBack()
    }

    override fun canGoForward(): Boolean {
        CsLogger.tag(tag).d("canGoForward")
        return webView.canGoForward()
    }

    override fun goForward() {
        CsLogger.tag(tag).d("goForward")
        webView.goForward()
    }

    override fun onPause() {
        CsLogger.tag(tag).d("onPause")
        webView.onPause()
    }

    override fun pauseTimers() {
        CsLogger.tag(tag).d("pauseTimers")
        webView.pauseTimers()
    }

    override fun saveState(outState: Bundle) {
        CsLogger.tag(tag).d("saveState")
        webView.saveState(outState)
    }

    override fun restoreState(savedInstanceState: Bundle) {
        CsLogger.tag(tag).d("restoreState")
        webView.restoreState(savedInstanceState)
    }

    override fun onResume() {
        CsLogger.tag(tag).d("onResume")
        webView.onResume()
    }

    override fun resumeTimers() {
        CsLogger.tag(tag).d("resumeTimers")
        webView.resumeTimers()
    }

    override fun clearCache(includeDiskFiles: Boolean) {
        CsLogger.tag(tag).d("clearCache")
        webView.clearCache(includeDiskFiles)
    }

    override fun clearHistory() {
        CsLogger.tag(tag).d("clearHistory")
        webView.clearHistory()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun destroy() {
        CsLogger.tag(tag).d("destroy")
        try {
            webView.parent?.let {
                if (it is ViewGroup) {
                    it.removeView(webView)
                }
            }
            webView.setOnTouchListener(null)
            webView.setOnKeyListener(null)
            webView.onFocusChangeListener = null
            webView.webChromeClient = null
            webView.loadUrl("about:blank")
            webView.onPause()
            webView.stopLoading()
            webView.removeAllViews()
            webView.destroyDrawingCache()
            webView.destroy()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).d(throwable)
        }
    }
}