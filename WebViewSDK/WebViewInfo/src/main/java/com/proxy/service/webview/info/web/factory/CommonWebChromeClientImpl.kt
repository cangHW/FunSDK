package com.proxy.service.webview.info.web.factory

import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.GeolocationPermissions
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.webview.base.constants.WebViewConstants
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.info.web.chooser.FileChooserParamsImpl
import com.proxy.service.webview.info.web.dialog.JsPromptResultImpl
import com.proxy.service.webview.info.web.dialog.JsResultImpl
import com.proxy.service.webview.info.web.permissions.GeolocationPermissionsCallbackImpl
import com.proxy.service.webview.info.web.permissions.PermissionRequestImpl

/**
 * @author: cangHX
 * @data: 2024/10/24 15:08
 * @desc:
 */
class CommonWebChromeClientImpl(
    private val loadCallback: WebLoadCallback?,
    private val interceptCallback: WebInterceptCallback?
) : WebChromeClient() {

    private val tag = "${WebViewConstants.LOG_TAG_START}ChromeClient"

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
//        super.onProgressChanged(view, newProgress)
        CsLogger.tag(tag)
            .d("onProgressChanged(view: WebView?, newProgress: Int) newProgress = $newProgress")
        try {
            loadCallback?.onProgressChanged(newProgress)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
//        super.onReceivedTitle(view, title)
        CsLogger.tag(tag).d("onReceivedTitle(view: WebView?, title: String?) title = $title")
        try {
            loadCallback?.onReceivedTitle(title ?: "")
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
//        super.onReceivedIcon(view, icon)
        CsLogger.tag(tag).d("onReceivedIcon(view: WebView?, icon: Bitmap?)")
        try {
            loadCallback?.onReceivedIcon(icon)
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
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
//        super.onRequestFocus(view)
        CsLogger.tag(tag).d("onRequestFocus(view: WebView?)")
        view?.requestFocus()
    }

    override fun onCloseWindow(view: WebView?) {
//        super.onCloseWindow(view)
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
            if (interceptCallback.onJsAlert(url ?: "", message ?: "", realResult)) {
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
            if (interceptCallback.onJsConfirm(url ?: "", message ?: "", realResult)) {
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
            if (interceptCallback.onJsPrompt(
                    url ?: "",
                    message ?: "",
                    defaultValue ?: "",
                    realResult
                )
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
//        super.onGeolocationPermissionsShowPrompt(origin, callback)
        try {
            if (interceptCallback != null) {
                val permissionsCallback = GeolocationPermissionsCallbackImpl(callback)
                interceptCallback.onGeolocationPermissionsShowPrompt(
                    origin ?: "",
                    permissionsCallback
                )
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onGeolocationPermissionsHidePrompt() {
//        super.onGeolocationPermissionsHidePrompt()
        CsLogger.tag(tag).d("onGeolocationPermissionsHidePrompt()")
        try {
            interceptCallback?.onGeolocationPermissionsHidePrompt()
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        CsLogger.tag(tag).d("onPermissionRequest(request: PermissionRequest?)")
//        super.onPermissionRequest(request)

        try {
            if (interceptCallback != null) {
                val permissionsCallback = PermissionRequestImpl(request)
                interceptCallback.onPermissionRequest(permissionsCallback)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
    }

    override fun onPermissionRequestCanceled(request: PermissionRequest?) {
        CsLogger.tag(tag).d("onPermissionRequestCanceled(request: PermissionRequest?)")
//        super.onPermissionRequestCanceled(request)

        try {
            if (interceptCallback != null) {
                val permissionsCallback = PermissionRequestImpl(request)
                interceptCallback.onPermissionRequestCanceled(permissionsCallback)
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(tag).e(throwable)
        }
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
        if (interceptCallback != null) {
            val chooserParams = FileChooserParamsImpl(fileChooserParams)
            val result = interceptCallback.onShowFileChooser(
                filePathCallback = object :
                    com.proxy.service.webview.base.web.callback.ValueCallback<Array<Uri>> {
                    override fun onReceiveValue(value: Array<Uri>) {
                        filePathCallback?.onReceiveValue(value)
                    }
                },
                chooserParams
            )
            if (result) {
                return true
            }
        }
        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
//        super.onShowCustomView(view, callback)
        CsLogger.tag(tag)
            .d("onShowCustomView(webView: View?, callback: CustomViewCallback?)")
    }

    override fun onHideCustomView() {
//        super.onHideCustomView()
        CsLogger.tag(tag)
            .d("onHideCustomView()")
    }

}