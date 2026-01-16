package com.proxy.service.webview.info.web.history

import android.webkit.WebBackForwardList
import com.proxy.service.webview.base.web.history.IWebBackForwardList
import com.proxy.service.webview.base.web.history.IWebHistoryItem

/**
 * @author: cangHX
 * @data: 2026/1/16 14:34
 * @desc:
 */
class WebBackForwardListImpl(
    private val webBackForwardList: WebBackForwardList
) : IWebBackForwardList {

    override fun getCurrentItem(): IWebHistoryItem? {
        val item = webBackForwardList.currentItem ?: return null
        return WebHistoryItemImpl(item)
    }

    override fun getCurrentIndex(): Int {
        return webBackForwardList.currentIndex
    }

    override fun getItemAtIndex(index: Int): IWebHistoryItem? {
        val size = getSize()
        if (index < 0 || index >= size) {
            return null
        }
        val item = webBackForwardList.getItemAtIndex(index)
        return WebHistoryItemImpl(item)
    }

    override fun getSize(): Int {
        return webBackForwardList.size
    }
}