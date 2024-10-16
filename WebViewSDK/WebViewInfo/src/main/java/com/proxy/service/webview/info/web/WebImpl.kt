package com.proxy.service.webview.info.web

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.base.web.callback.ValueCallback
import com.proxy.service.webview.info.config.Config
import com.proxy.service.webview.info.view.WebViewImpl
import com.proxy.service.webview.info.view.group.IFactory
import com.proxy.service.webview.info.web.callback.ValueCallbackImpl
import com.proxy.service.webview.info.web.lifecycle.LifecycleObserverImpl
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/8/1 15:41
 * @desc:
 */
class WebImpl(private val webView: WebViewImpl, private val lifecycleOwner: LifecycleOwner?) :
    IWeb {

    private val tag = "${Config.LOG_TAG_START}IWeb"

    private val isDestroy = AtomicBoolean(false)
    private val lifecycleObserver = LifecycleObserverImpl(this)

    init {
        lifecycleOwner?.lifecycle?.addObserver(lifecycleObserver)
    }

    override fun removeFromParent() {
        CsLogger.tag(tag).d("removeFromParent")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.parent?.let {
            if (it is ViewGroup) {
                it.removeView(webView)
            }
        }
    }

    override fun changeParentView(viewGroup: ViewGroup?) {
        CsLogger.tag(tag).d("changeParentView viewGroup = $viewGroup")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        if (viewGroup == null) {
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
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.setBackgroundColor(color)
    }

    override fun setVisibility(visibility: Int) {
        CsLogger.tag(tag).d("setVisibility visibility = $visibility")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.visibility = visibility
    }

    override fun setLayerType(layerType: Int, paint: Paint?) {
        CsLogger.tag(tag).d("setLayerType layerType = $layerType, paint = $paint")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.setLayerType(layerType, paint)
    }

    override fun evaluateJavascript(script: String, resultCallback: ValueCallback<String?>?) {
        CsLogger.tag(tag).d("evaluateJavascript script = $script")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                webView.evaluateJavascript(script, ValueCallbackImpl(resultCallback))
                return ""
            }
        })?.start()
    }

    override fun loadUrl(url: String) {
        CsLogger.tag(tag).d("loadUrl url = $url")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.loadUrl(url)
    }

    override fun reload() {
        CsLogger.tag(tag).d("reload")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.reload()
    }

    override fun canGoBack(): Boolean {
        CsLogger.tag(tag).d("canGoBack")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return false
        }
        return webView.canGoBack()
    }

    override fun goBack() {
        CsLogger.tag(tag).d("goBack")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.goBack()
    }

    override fun canGoForward(): Boolean {
        CsLogger.tag(tag).d("canGoForward")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return false
        }
        return webView.canGoForward()
    }

    override fun goForward() {
        CsLogger.tag(tag).d("goForward")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.goForward()
    }

    override fun onPause() {
        CsLogger.tag(tag).d("onPause")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.onPause()
    }

    override fun pauseTimers() {
        CsLogger.tag(tag).d("pauseTimers")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.pauseTimers()
    }

    override fun saveState(outState: Bundle) {
        CsLogger.tag(tag).d("saveState outState = $outState")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.saveState(outState)
    }

    override fun restoreState(savedInstanceState: Bundle) {
        CsLogger.tag(tag).d("restoreState savedInstanceState = $savedInstanceState")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.restoreState(savedInstanceState)
    }

    override fun onResume() {
        CsLogger.tag(tag).d("onResume")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.onResume()
    }

    override fun resumeTimers() {
        CsLogger.tag(tag).d("resumeTimers")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.resumeTimers()
    }

    override fun clearCache(includeDiskFiles: Boolean) {
        CsLogger.tag(tag).d("clearCache includeDiskFiles = $includeDiskFiles")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.clearCache(includeDiskFiles)
    }

    override fun clearHistory() {
        CsLogger.tag(tag).d("clearHistory")
        if (isDestroy.get()) {
            CsLogger.tag(tag).d("当前 web 容器已销毁")
            return
        }
        webView.clearHistory()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun destroy() {
        CsLogger.tag(tag).d("destroy")
        if (!isDestroy.compareAndSet(false, true)) {
            CsLogger.tag(tag).d("当前 web 容器已销毁, 不需要多次销毁")
            return
        }
        lifecycleOwner?.lifecycle?.removeObserver(lifecycleObserver)
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
//            webView.loadUrl("about:blank")
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