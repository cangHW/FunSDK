package com.proxy.service.webview.base.enums

import android.webkit.WebSettings

/**
 * @author: cangHX
 * @data: 2024/8/2 11:01
 * @desc:
 */
enum class CacheMode(val mode: Int) {

    /**
     * 根据HTTP头决定是否从网络加载内容
     * */
    LOAD_DEFAULT(WebSettings.LOAD_DEFAULT),

    /**
     * 如果缓存可用，则使用缓存，否则从网络加载
     * */
    LOAD_CACHE_ELSE_NETWORK(WebSettings.LOAD_CACHE_ELSE_NETWORK),

    /**
     * 不使用缓存，从网络加载所有资源
     * */
    LOAD_NO_CACHE(WebSettings.LOAD_NO_CACHE),

    /**
     * 只使用缓存，不从网络加载任何内容
     * */
    LOAD_CACHE_ONLY(WebSettings.LOAD_CACHE_ONLY);

}