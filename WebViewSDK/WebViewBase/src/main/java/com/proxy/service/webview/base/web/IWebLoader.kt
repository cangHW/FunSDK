package com.proxy.service.webview.base.web

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.webview.base.listener.WebInterceptCallback
import com.proxy.service.webview.base.listener.WebLoadCallback

/**
 * @author: cangHX
 * @data: 2024/7/31 18:40
 * @desc:
 */
interface IWebLoader {

    /**
     * 加载页面
     * */
    fun loadUrl(url: String): IWebLoader

    /**
     * 设置 bridge，允许设置多个，最终合并到同一默认命名空间（_android）
     * */
    fun addJavascriptInterface(any: Any): IWebLoader

    /**
     * 设置 bridge，允许设置多个，如果命名空间名称相同，则合并
     * */
    fun addJavascriptInterface(nameSpace: String, any: Any): IWebLoader

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): IWebLoader

    /**
     * 设置加载回调，用于展示加载动画、错误页面等
     * */
    fun setLoadCallback(callback: WebLoadCallback): IWebLoader

    /**
     * 设置加载拦截器，用于拦截请求、键盘事件等
     * */
    fun setInterceptCallback(callback: WebInterceptCallback): IWebLoader

    /**
     * 加载 web 容器到 ViewGroup
     * */
    fun loadTo(viewGroup: ViewGroup?): IWeb

    /**
     * 加载 web 容器
     * */
    fun load(): IWeb
}