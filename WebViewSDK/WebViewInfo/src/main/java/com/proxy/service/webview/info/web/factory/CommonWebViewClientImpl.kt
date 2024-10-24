package com.proxy.service.webview.info.web.factory

import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.KeyEvent
import android.webkit.ClientCertRequest
import android.webkit.HttpAuthHandler
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.info.config.Config
import com.proxy.service.webview.info.web.error.SslErrorHandlerImpl
import com.proxy.service.webview.info.web.error.SslErrorImpl
import com.proxy.service.webview.info.web.intercept.WebResourceRequestImpl

/**
 * @author: cangHX
 * @data: 2024/10/24 15:08
 * @desc:
 */
class CommonWebViewClientImpl(
    private val webResourceAssetLoader: WebViewAssetLoader?,
    private val loadCallback: WebLoadCallback?,
    private val interceptCallback: WebInterceptCallback?
) : WebViewClient() {

    private val tag = "${Config.LOG_TAG_START}Client"

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        CsLogger.tag(tag).d("shouldOverrideUrlLoading(view: WebView, url: String) url = $url")
        if (interceptCallback?.shouldOverrideUrlLoading(url) == true) {
            return true
        }
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        CsLogger.tag(tag)
            .d("shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) url = ${request.url}")
        val wrr = WebResourceRequestImpl(request)
        if (interceptCallback?.shouldOverrideUrlLoading(wrr) == true) {
            return true
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun shouldOverrideKeyEvent(view: WebView, keyEvent: KeyEvent): Boolean {
        CsLogger.tag(tag)
            .d("shouldOverrideKeyEvent(view: WebView, keyEvent: KeyEvent) keyEvent = $keyEvent")
        if (interceptCallback?.shouldOverrideKeyEvent(keyEvent) == true) {
            return true
        }
        return super.shouldOverrideKeyEvent(view, keyEvent)
    }

    override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
        CsLogger.tag(tag).d("shouldInterceptRequest(view: WebView, url: String) url = $url")
        var response: WebResourceResponse? = null
        interceptCallback?.shouldInterceptRequest(url)?.let {
            response = WebResourceResponse(
                it.getMimeType(),
                it.getEncoding(),
                it.getStatusCode(),
                it.getReasonPhrase(),
                it.getResponseHeaders(),
                it.getData()
            )
        }

        if (response != null) {
            return response
        }

        response = webResourceAssetLoader?.shouldInterceptRequest(Uri.parse(url))
        if (response != null) {
            return response
        }

        return super.shouldInterceptRequest(view, url)
    }

    override fun shouldInterceptRequest(
        view: WebView,
        request: WebResourceRequest
    ): WebResourceResponse? {
        CsLogger.tag(tag)
            .d("shouldInterceptRequest(view: WebView, request: WebResourceRequest) url = ${request.url}")
        var response: WebResourceResponse? = null
        val rq = WebResourceRequestImpl(request)
        interceptCallback?.shouldInterceptRequest(rq)?.let {
            response = WebResourceResponse(
                it.getMimeType(),
                it.getEncoding(),
                it.getStatusCode(),
                it.getReasonPhrase(),
                it.getResponseHeaders(),
                it.getData()
            )
        }

        if (response != null) {
            return response
        }

        response = webResourceAssetLoader?.shouldInterceptRequest(request.url)
        if (response != null) {
            return response
        }

        return super.shouldInterceptRequest(view, request)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        CsLogger.tag(tag)
            .d("onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) url = $url")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                loadCallback?.onPageStarted(url ?: "")
                return ""
            }
        })?.start()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        CsLogger.tag(tag).d("onPageFinished(view: WebView?, url: String?) url = $url")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                loadCallback?.onPageFinished(url ?: "")
                return ""
            }
        })?.start()
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        CsLogger.tag(tag).d("onPageCommitVisible(view: WebView?, url: String?) url = $url")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                loadCallback?.onPageCommitVisible(url ?: "")
                return ""
            }
        })?.start()
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        super.onReceivedError(view, request, error)
        CsLogger.tag(tag)
            .d("onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) url = ${request.url}, error = ${error.errorCode}:${error.description}")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (request.isForMainFrame) {
                    loadCallback?.onPageError(
                        url = request.url?.toString() ?: "",
                        isMainFrameError = true,
                        isHttpError = false
                    )
                } else {
                    loadCallback?.onPageError(
                        url = request.url?.toString() ?: "",
                        isMainFrameError = false,
                        isHttpError = false
                    )
                }
                return ""
            }
        })?.start()
    }

    override fun onReceivedHttpError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceResponse
    ) {
        super.onReceivedHttpError(view, request, error)
        CsLogger.tag(tag)
            .d("onReceivedHttpError(view: WebView, request: WebResourceRequest, error: WebResourceResponse) url = ${request.url}, error = ${error.statusCode}:${error.responseHeaders}")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (request.isForMainFrame) {
                    loadCallback?.onPageError(
                        url = request.url?.toString() ?: "",
                        isMainFrameError = true,
                        isHttpError = true
                    )
                } else {
                    loadCallback?.onPageError(
                        url = request.url?.toString() ?: "",
                        isMainFrameError = false,
                        isHttpError = true
                    )
                }
                return ""
            }
        })?.start()
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        CsLogger.tag(tag)
            .d("onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) url = ${error.url}")
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val sslError = SslErrorImpl(error)
                val sslErrorHandler = SslErrorHandlerImpl(handler)
                loadCallback?.onPageSslError(sslError, sslErrorHandler)
                return ""
            }
        })?.start()
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
        CsLogger.tag(tag).d("onLoadResource(view: WebView?, url: String?) url = $url")
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        super.onReceivedClientCertRequest(view, request)
        CsLogger.tag(tag)
            .d("onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) host = ${request?.host}, port = ${request?.port}")
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm)
        CsLogger.tag(tag)
            .d("onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) host = ${host}, realm = $realm")
    }

    override fun onReceivedLoginRequest(
        view: WebView?,
        realm: String?,
        account: String?,
        args: String?
    ) {
        super.onReceivedLoginRequest(view, realm, account, args)
        CsLogger.tag(tag)
            .d("onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) realm = $realm, account = $account, args = $args")
    }

    override fun onRenderProcessGone(
        view: WebView?,
        detail: RenderProcessGoneDetail?
    ): Boolean {
        CsLogger.tag(tag)
            .d("onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?)")
        if (detail != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (detail.didCrash()) {
                    CsLogger.tag(tag).e("The WebView rendering process crashed!")
                } else {
                    CsLogger.tag(tag)
                        .e("The WebView rendering process was killed by the system.")
                }
            }
        }

        view?.let {
            it.loadUrl("about:blank")
            it.reload()
        }

        return true
    }

}