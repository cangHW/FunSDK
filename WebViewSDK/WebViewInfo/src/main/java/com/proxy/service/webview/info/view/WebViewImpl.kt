package com.proxy.service.webview.info.view

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.webkit.ClientCertRequest
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.HttpAuthHandler
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.info.config.Config
import com.proxy.service.webview.info.web.dialog.JsPromptResultImpl
import com.proxy.service.webview.info.web.dialog.JsResultImpl
import com.proxy.service.webview.info.web.error.SslErrorHandlerImpl
import com.proxy.service.webview.info.web.error.SslErrorImpl
import com.proxy.service.webview.info.web.intercept.WebResourceRequestImpl
import com.proxy.service.webview.info.web.permissions.GeolocationPermissionsCallbackImpl
import com.proxy.service.webview.info.web.permissions.PermissionRequestImpl

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

    private val tag = "${Config.LOG_TAG_START}View"
    private var loadCallback: WebLoadCallback? = null
    private var interceptCallback: WebInterceptCallback? = null
    private var webResourceAssetLoader: WebViewAssetLoader? = null

    fun setConfig(config: WebConfig) {
        settings.domStorageEnabled = true
        CookieManager.getInstance()
            .setAcceptThirdPartyCookies(this, config.isAcceptThirdPartyCookies())
        settings.mixedContentMode = config.getMixedContentMode().mode
        settings.javaScriptEnabled = config.isJavaScriptEnabled()
        settings.cacheMode = config.getCacheMode().mode
        settings.useWideViewPort = config.isUseWideViewPort()
        settings.loadWithOverviewMode = config.isLoadWithOverviewMode()
        if (config.isUserAgentAppend()) {
            settings.userAgentString = "${settings.userAgentString} ${config.getUserAgent()}"
        } else {
            settings.userAgentString = config.getUserAgent()
        }
        settings.savePassword = config.isSavePassword()
        settings.allowFileAccess = config.isAllowFileAccess()
        settings.allowContentAccess = config.isAllowContentAccess()
        settings.allowFileAccessFromFileURLs = config.isAllowFileAccessFromFileURLs()
        settings.allowUniversalAccessFromFileURLs = config.isAllowUniversalAccessFromFileURLs()

        webResourceAssetLoader = config.getWebViewAssetLoader()

        isVerticalScrollBarEnabled = config.isVerticalScrollBarEnabled()
        isHorizontalScrollBarEnabled = config.isHorizontalScrollBarEnabled()
        isHorizontalFadingEdgeEnabled = config.isHorizontalFadingEdgeEnabled()

        webViewClient = commonWebViewClient
        webChromeClient = commonWebChromeClient
    }

    fun setLoadCallback(callback: WebLoadCallback?) {
        this.loadCallback = callback
    }

    fun setInterceptCallback(callback: WebInterceptCallback?) {
        this.interceptCallback = callback
    }

    private val commonWebViewClient = object : WebViewClient() {

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

    private val commonWebChromeClient = object : WebChromeClient() {
        private val tag = "${Config.LOG_TAG_START}ChromeClient"

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            CsLogger.tag(tag)
                .d("onProgressChanged(view: WebView?, newProgress: Int) newProgress = $newProgress")
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    loadCallback?.onProgressChanged(newProgress)
                    return ""
                }
            })?.start()
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            CsLogger.tag(tag).d("onReceivedTitle(view: WebView?, title: String?) title = $title")
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    loadCallback?.onReceivedTitle(title ?: "")
                    return ""
                }
            })?.start()
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
            super.onReceivedIcon(view, icon)
            CsLogger.tag(tag).d("onReceivedIcon(view: WebView?, icon: Bitmap?)")
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    loadCallback?.onReceivedIcon(icon)
                    return ""
                }
            })?.start()
        }

        override fun onCreateWindow(
            view: WebView?,
            isDialog: Boolean,
            isUserGesture: Boolean,
            resultMsg: Message?
        ): Boolean {
            CsLogger.tag(tag)
                .d("onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?) isDialog = $isDialog, isUserGesture = $isUserGesture")
            if (interceptCallback?.onCreateWindow(isDialog, isUserGesture, resultMsg) == true) {
                return true
            }
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
        }

        override fun onRequestFocus(view: WebView?) {
            super.onRequestFocus(view)
            CsLogger.tag(tag).d("onRequestFocus(view: WebView?)")
            view?.requestFocus()
        }

        override fun onCloseWindow(view: WebView?) {
            super.onCloseWindow(view)
            CsLogger.tag(tag).d("onCloseWindow(view: WebView?)")
            interceptCallback?.onCloseWindow()
        }

        override fun onJsAlert(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            CsLogger.tag(tag)
                .d("onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?) url = $url, message = $message")
            if (interceptCallback != null) {
                val realResult = JsResultImpl(result)
                if (interceptCallback?.onJsAlert(url ?: "", message ?: "", realResult) == true) {
                    return true
                }
            }
            return super.onJsAlert(view, url, message, result)
        }

        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            CsLogger.tag(tag)
                .d("onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?) url = $url, message = $message")
            if (interceptCallback != null) {
                val realResult = JsResultImpl(result)
                if (interceptCallback?.onJsConfirm(url ?: "", message ?: "", realResult) == true) {
                    return true
                }
            }
            return super.onJsConfirm(view, url, message, result)
        }

        override fun onJsPrompt(
            view: WebView?,
            url: String?,
            message: String?,
            defaultValue: String?,
            result: JsPromptResult?
        ): Boolean {
            CsLogger.tag(tag)
                .d("onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?) url = $url, message = $message, defaultValue = $defaultValue")
            if (interceptCallback != null) {
                val realResult = JsPromptResultImpl(result)
                if (interceptCallback?.onJsPrompt(
                        url ?: "",
                        message ?: "",
                        defaultValue ?: "",
                        realResult
                    ) == true
                ) {
                    return true
                }
            }
            return super.onJsPrompt(view, url, message, defaultValue, result)
        }

        override fun onJsBeforeUnload(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult?
        ): Boolean {
            CsLogger.tag(tag)
                .d("onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?) url = $url, message = $message")
            return super.onJsBeforeUnload(view, url, message, result)
        }

        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback?
        ) {
            CsLogger.tag(tag)
                .d("onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissionsCallback?) origin = $origin")
            super.onGeolocationPermissionsShowPrompt(origin, callback)
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    if (interceptCallback != null) {
                        val permissionsCallback = GeolocationPermissionsCallbackImpl(callback)
                        interceptCallback?.onGeolocationPermissionsShowPrompt(
                            origin ?: "",
                            permissionsCallback
                        )
                    }
                    return ""
                }
            })?.start()
        }

        override fun onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt()
            CsLogger.tag(tag).d("onGeolocationPermissionsHidePrompt()")
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    if (interceptCallback != null) {
                        interceptCallback?.onGeolocationPermissionsHidePrompt()
                    }
                    return ""
                }
            })?.start()
        }

        override fun onPermissionRequest(request: PermissionRequest?) {
            CsLogger.tag(tag).d("onPermissionRequest(request: PermissionRequest?)")
            super.onPermissionRequest(request)
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    if (interceptCallback != null) {
                        val permissionsCallback = PermissionRequestImpl(request)
                        interceptCallback?.onPermissionRequest(permissionsCallback)
                    }
                    return ""
                }
            })?.start()
        }

        override fun onPermissionRequestCanceled(request: PermissionRequest?) {
            CsLogger.tag(tag).d("onPermissionRequestCanceled(request: PermissionRequest?)")
            super.onPermissionRequestCanceled(request)
            CsTask.mainThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    if (interceptCallback != null) {
                        val permissionsCallback = PermissionRequestImpl(request)
                        interceptCallback?.onPermissionRequestCanceled(permissionsCallback)
                    }
                    return ""
                }
            })?.start()
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            CsLogger.tag(tag)
                .d("ConsoleMessage level : ${consoleMessage?.messageLevel()?.name}, sourceID : ${consoleMessage?.sourceId()}, line : ${consoleMessage?.lineNumber()}, message : ${consoleMessage?.message()}")
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            CsLogger.tag(tag)
                .d("onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?)")
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }

        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
            super.onShowCustomView(view, callback)
            CsLogger.tag(tag)
                .d("onShowCustomView(webView: View?, callback: CustomViewCallback?)")
        }

        override fun onHideCustomView() {
            super.onHideCustomView()
            CsLogger.tag(tag)
                .d("onHideCustomView()")
        }

    }

    override fun loadUrl(url: String) {
        super.loadUrl(url)
        CsLogger.tag(tag).d("loadUrl(url: String) url = $url")
    }

}