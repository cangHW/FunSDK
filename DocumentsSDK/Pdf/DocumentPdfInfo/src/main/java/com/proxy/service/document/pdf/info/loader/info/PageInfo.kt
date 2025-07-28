package com.proxy.service.document.pdf.info.loader.info

import android.graphics.Bitmap
import android.view.Surface
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.document.pdf.base.bean.LinkData
import com.proxy.service.document.pdf.info.core.PdfiumCore

/**
 * @author: cangHX
 * @data: 2025/4/30 17:55
 * @desc:
 */
class PageInfo(private val doc_hand: Long, val page_hand: Long) {

    companion object {
        private var dpi = -1

        private fun getDpi(): Int {
            if (dpi == -1) {
                dpi = CsContextManager.getApplication().resources.displayMetrics.densityDpi
            }
            return dpi
        }
    }

    /**
     * 当前页面内的超链接映射
     * */
    private var links: ArrayList<LinkData>? = null

    /**
     * 当前页面宽度
     * */
    private var widthPixel: Int = -1

    /**
     * 当前页面宽度
     * */
    private var widthPoint: Int = -1

    /**
     * 当前页面高度
     * */
    private var heightPixel: Int = -1

    /**
     * 当前页面高度
     * */
    private var heightPoint: Int = -1

    /**
     * 获取超链接
     * */
    fun getLinks(): ArrayList<LinkData> {
        if (links == null) {
            links = PdfiumCore.getInstance().getPageLinks(doc_hand, page_hand)
        }
        return links ?: ArrayList()
    }

    /**
     * 获取文档页面宽度
     * */
    fun getPageWidthPixel(): Int {
        if (widthPixel == -1) {
            widthPixel = PdfiumCore.getInstance().nativeGetPageWidthPixel(page_hand, getDpi())
        }
        return widthPixel
    }

    /**
     * 获取文档页面宽度
     * */
    fun getPageWidthPoint(): Int {
        if (widthPoint == -1) {
            widthPoint = PdfiumCore.getInstance().nativeGetPageWidthPoint(page_hand)
        }
        return widthPoint
    }

    /**
     * 获取文档页面高度
     * */
    fun getPageHeightPixel(): Int {
        if (heightPixel == -1) {
            heightPixel = PdfiumCore.getInstance().nativeGetPageHeightPixel(page_hand, getDpi())
        }
        return heightPixel
    }

    /**
     * 获取文档页面高度
     * */
    fun getPageHeightPoint(): Int {
        if (heightPoint == -1) {
            heightPoint = PdfiumCore.getInstance().nativeGetPageHeightPoint(page_hand)
        }
        return heightPoint
    }

    /**
     * 渲染页面内容到 bitmap
     * */
    fun renderPageToBitmap(
        bitmap: Bitmap,
        startX: Int,
        startY: Int,
        drawSizeHor: Int,
        drawSizeVer: Int,
        renderAnnot: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        PdfiumCore.getInstance().nativeRenderPageToBitmap(
            page_hand,
            bitmap,
            getDpi(),
            startX,
            startY,
            drawSizeHor,
            drawSizeVer,
            renderAnnot,
            viewBgColor,
            pageBgColor
        )
    }

    /**
     * 渲染页面内容到 surface
     * */
    fun renderPageToSurface(
        surface: Surface,
        startX: Int,
        startY: Int,
        drawSizeHor: Int,
        drawSizeVer: Int,
        renderAnnot: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        PdfiumCore.getInstance().nativeRenderPageToSurface(
            page_hand,
            surface,
            getDpi(),
            startX,
            startY,
            drawSizeHor,
            drawSizeVer,
            renderAnnot,
            viewBgColor,
            pageBgColor
        )
    }


}