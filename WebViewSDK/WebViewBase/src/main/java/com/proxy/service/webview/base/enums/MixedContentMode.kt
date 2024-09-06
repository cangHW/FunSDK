package com.proxy.service.webview.base.enums

import android.webkit.WebSettings

/**
 * @author: cangHX
 * @data: 2024/8/1 19:53
 * @desc:
 */
enum class MixedContentMode(val mode: Int) {

    /**
     * 不允许任何混合内容
     * */
    MIXED_CONTENT_NEVER_ALLOW(WebSettings.MIXED_CONTENT_NEVER_ALLOW),

    /**
     * 允许所有混合内容
     * */
    MIXED_CONTENT_ALWAYS_ALLOW(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW),

    /**
     * 允许安全来源（HTTPS）加载非安全来源（HTTP）的资源，但当前页面必须是 HTTPS
     * */
    MIXED_CONTENT_COMPATIBILITY_MODE(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);

}