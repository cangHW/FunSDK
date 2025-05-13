package com.proxy.service.webview.base.listener

import android.os.Message
import android.view.KeyEvent
import com.proxy.service.webview.base.web.dialog.JsPromptResult
import com.proxy.service.webview.base.web.dialog.JsResult
import com.proxy.service.webview.base.web.permissions.GeolocationPermissionsCallback
import com.proxy.service.webview.base.web.permissions.PermissionRequest
import com.proxy.service.webview.base.web.request.WebResourceRequest
import com.proxy.service.webview.base.web.request.WebResourceResponse

/**
 * @author: cangHX
 * @data: 2024/8/3 11:39
 * @desc:
 */
interface WebInterceptCallback {

    /**
     * 是否应该进行对应 url 加载
     * */
    fun shouldOverrideUrlLoading(url: String): Boolean {
        return false
    }

    /**
     * 是否应该进行对应 url 加载
     * */
    fun shouldOverrideUrlLoading(request: WebResourceRequest): Boolean {
        return shouldOverrideUrlLoading(request.getUrl().toString())
    }

    /**
     * 是否应该拦截键盘事件
     * */
    fun shouldOverrideKeyEvent(keyEvent: KeyEvent): Boolean {
        return false
    }

    /**
     * 是否拦截加载过程
     * */
    fun shouldInterceptRequest(url: String): WebResourceResponse? {
        return null
    }

    /**
     * 是否拦截加载过程
     * */
    fun shouldInterceptRequest(request: WebResourceRequest): WebResourceResponse? {
        return shouldInterceptRequest(request.getUrl().toString())
    }

    /**
     * 当网页尝试打开一个新窗口时会被调用。这个方法允许你控制新的WebView实例的创建，以便处理弹出窗口或其他类似的情况
     * */
    fun onCreateWindow(isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
        return false
    }

    /**
     * 当网页请求关闭窗口时会被调用。与 [onCreateWindow] 共同出现
     * */
    fun onCloseWindow() {}

    /**
     * 处理 JavaScript 弹出警告框（alert）的情况
     * */
    fun onJsAlert(url: String, message: String, result: JsResult): Boolean {
        return false
    }

    /**
     * 处理 JavaScript 确认对话框（confirm）的情况
     * */
    fun onJsConfirm(url: String, message: String, result: JsResult): Boolean {
        return false
    }

    /**
     * 处理 JavaScript 提示对话框（prompt）的情况
     * */
    fun onJsPrompt(
        url: String,
        message: String,
        defaultValue: String,
        result: JsPromptResult
    ): Boolean {
        return false
    }

    /**
     * 当网页请求访问地理位置时会被调用
     * */
    fun onGeolocationPermissionsShowPrompt(
        origin: String,
        callback: GeolocationPermissionsCallback
    ){}

    /**
     * 处理地理位置权限请求的隐藏提示
     * */
    fun onGeolocationPermissionsHidePrompt() {}

    /**
     * 用于处理 WebView 中的权限请求。当网页尝试访问某些需要用户授权的功能（例如摄像头、麦克风等）时，会触发这个方法。
     * */
    fun onPermissionRequest(request: PermissionRequest) {
        request.deny()
    }

    /**
     * 取消处理 WebView 中的权限请求。
     * */
    fun onPermissionRequestCanceled(request: PermissionRequest) {
    }

}