package com.proxy.service.webview.info.web.history

import android.graphics.Bitmap
import android.webkit.WebHistoryItem
import com.proxy.service.webview.base.web.history.IWebHistoryItem

/**
 * @author: cangHX
 * @data: 2026/1/16 14:35
 * @desc:
 */
class WebHistoryItemImpl(
    private val webHistoryItem: WebHistoryItem
): IWebHistoryItem {

    override fun getUrl(): String {
        return webHistoryItem.url
    }

    override fun getOriginalUrl(): String {
        return webHistoryItem.originalUrl
    }

    override fun getTitle(): String {
        return webHistoryItem.title
    }

    override fun getFavicon(): Bitmap? {
        return webHistoryItem.favicon
    }
}