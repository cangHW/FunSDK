package com.proxy.service.webview.info.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.webkit.WebView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.base.constants.WebViewConstants
import com.proxy.service.webview.base.listener.WebLifecycleCallback

/**
 * @author: cangHX
 * @data: 2024/8/1 15:11
 * @desc:
 */
class WebViewImpl : WebView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val tag = "${WebViewConstants.LOG_TAG_START}View"

    private var webLifecycleCallback: WebLifecycleCallback? = null

    fun setWebLifecycleCallback(webLifecycleCallback: WebLifecycleCallback?) {
        this.webLifecycleCallback = webLifecycleCallback
    }

    override fun loadUrl(url: String) {
        super.loadUrl(url)
        CsLogger.tag(tag).d("loadUrl(url: String) url = $url")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        CsLogger.tag(tag).d("onAttachedToWindow")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                webLifecycleCallback?.onAttachedToWindow()
                return ""
            }
        })?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        CsLogger.tag(tag).d("onDetachedFromWindow")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                webLifecycleCallback?.onDetachedFromWindow()
                return ""
            }
        })?.start()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        CsLogger.tag(tag).d("onKeyDown")
        try {
            if (webLifecycleCallback?.onKeyDown(keyCode, event) == true) {
                return true
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        CsLogger.tag(tag).d("onKeyLongPress")
        try {
            if (webLifecycleCallback?.onKeyLongPress(keyCode, event) == true) {
                return true
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        CsLogger.tag(tag).d("onKeyUp")
        try {
            if (webLifecycleCallback?.onKeyUp(keyCode, event) == true) {
                return true
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        try {
            if (webLifecycleCallback?.onTouchEvent(event) == true) {
                return true
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
        return super.onTouchEvent(event)
    }

}