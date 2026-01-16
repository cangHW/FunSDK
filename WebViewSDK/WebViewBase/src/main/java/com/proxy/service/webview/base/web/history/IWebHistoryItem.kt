package com.proxy.service.webview.base.web.history

import android.graphics.Bitmap

/**
 * @author: cangHX
 * @data: 2026/1/16 14:25
 * @desc:
 */
interface IWebHistoryItem {

    /**
     * 获取当前记录项加载的 url
     * */
    fun getUrl(): String

    /**
     * 获取当前记录项最初加载的 url, 无论是否发生了重定向
     * */
    fun getOriginalUrl(): String

    /**
     * 获取当前记录项页面标题
     * */
    fun getTitle(): String

    /**
     * 获取当前记录项页面图标
     * */
    fun getFavicon(): Bitmap?
}