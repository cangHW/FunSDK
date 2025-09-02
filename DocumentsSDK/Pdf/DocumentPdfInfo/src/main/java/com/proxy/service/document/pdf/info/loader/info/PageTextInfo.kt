package com.proxy.service.document.pdf.info.loader.info

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.pdf.info.core.PdfiumCore

/**
 * @author: cangHX
 * @data: 2025/8/25 10:41
 * @desc:
 */
class PageTextInfo(private val page_hand: Long) {

    private val any = Any()

    private var page_text_hand: Long = 0L

    private var textCount: Int = -1
    private var text: String? = null

    /**
     * 加载页面内容
     * */
    private fun load() {
        synchronized(any) {
            if (isLoaded()) {
                return
            }
            page_text_hand = PdfiumCore.getInstance().nativeLoadTextPage(page_hand)
        }
    }

    /**
     * 清理缓存数据
     * */
    fun clear() {
        synchronized(any) {
            if (isLoaded()) {
                PdfiumCore.getInstance().nativeCloseTextPage(page_text_hand)
                page_text_hand = 0L
                textCount = -1
                text = null
            }
        }
    }

    /**
     * 是否加载完成
     * */
    private fun isLoaded(): Boolean {
        synchronized(any) {
            return page_text_hand != 0L
        }
    }

    /**
     * 获取文字数量
     * */
    fun getTextCount(): Int {
        synchronized(any) {
            if (!isLoaded()) {
                load()
            }
            if (textCount == -1) {
                textCount = PdfiumCore.getInstance().nativeGetCharsCount(page_text_hand)
            }
            return textCount
        }
    }

    /**
     * 获取页面内文字
     * */
    fun getText(): String {
        synchronized(any) {
            if (!isLoaded()) {
                load()
            }
            if (text == null) {
                text = PdfiumCore.getInstance().nativeGetText(page_text_hand, 0, getTextCount())
            }
            return text ?: ""
        }
    }


}