package com.proxy.service.document.pdf.loader.info

import android.util.ArrayMap
import com.proxy.service.document.pdf.core.PdfiumCore
import com.proxy.service.document.pdf.loader.source.BaseController

/**
 * @author: cangHX
 * @data: 2025/4/30 17:37
 * @desc:
 */
class DocumentInfo(private val controller: BaseController, val hand: Long) {

    /**
     * 页面映射
     * */
    private val pageMap = ArrayMap<Int, PageInfo>()

    /**
     * 页面数量
     * */
    private var pageCount = -1

    /**
     * 开始页码
     * */
    private var startIndex = -1

    /**
     * 结束页码
     * */
    private var endIndex = -1

    /**
     * 获取文档页面数量
     * */
    fun getPageCount(): Int {
        if (pageCount == -1) {
            pageCount = PdfiumCore.getInstance().nativeGetPageCount(hand)
        }
        if (pageCount == -1) {
            pageCount = 0
        }
        return pageCount
    }

    /**
     * 设置开始页码
     * */
    fun setPageStart(startIndex: Int) {
        this.startIndex = startIndex
    }

    /**
     * 获取开始页码
     * */
    fun getPageStart(): Int {
        return startIndex
    }

    /**
     * 获取结束页码
     * */
    fun getPageEnd(): Int {
        if (endIndex == -1) {
            endIndex = getPageStart() + getPageCount() - 1
        }
        return endIndex
    }

    /**
     * 获取页面信息
     * */
    fun getPageInfo(pageIndex: Int): PageInfo? {
        if (pageIndex < getPageStart()) {
            return null
        }
        if (pageIndex > getPageEnd()) {
            return null
        }
        val real_index = pageIndex - getPageStart()
        var page = pageMap.get(real_index)
        if (page == null) {
            val page_hand = PdfiumCore.getInstance().nativeLoadPage(hand, real_index)
            page = PageInfo(hand, page_hand)
            pageMap.put(real_index, page)
        }
        return page
    }

    /**
     * 关闭文档
     * */
    fun close() {
        val list = ArrayList<Long>()
        pageMap.forEach {
            list.add(it.value.page_hand)
        }
        PdfiumCore.getInstance().nativeClosePages(list.toLongArray())
        pageMap.clear()
        PdfiumCore.getInstance().nativeCloseDocument(hand)
        controller.destroy()
    }
}