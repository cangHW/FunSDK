package com.proxy.service.document.pdf.info.loader.impl

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.view.Surface
import com.proxy.service.document.pdf.base.loader.IPdfRender
import kotlin.math.min


/**
 * @author: cangHX
 * @data: 2025/4/30 14:59
 * @desc:
 */
open class PdfRender : PdfAction(), IPdfRender {

    override fun renderPageToBitmap(
        pageIndex: Int,
        bitmap: Bitmap,
        renderAnnot: Boolean,
        autoSize: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        renderPageToBitmap(
            pageIndex,
            bitmap,
            bitmap.width,
            bitmap.height,
            renderAnnot,
            autoSize,
            viewBgColor,
            pageBgColor
        )
    }

    override fun renderPageToBitmap(
        pageIndex: Int,
        bitmap: Bitmap,
        width: Int,
        height: Int,
        renderAnnot: Boolean,
        autoSize: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        renderPageToBitmap(
            pageIndex,
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            renderAnnot,
            autoSize,
            viewBgColor,
            pageBgColor
        )
    }

    override fun renderPageToBitmap(
        pageIndex: Int,
        bitmap: Bitmap,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        renderAnnot: Boolean,
        autoSize: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        synchronized(lock) {
            val page = findDocByPageIndex(pageIndex)
                ?.getPageInfo(pageIndex)
                ?: return

            val realWidth = endX - startX
            val realHeight = endY - startY

            if (!autoSize) {
                page.renderPageToBitmap(
                    bitmap,
                    startX,
                    startY,
                    realWidth,
                    realHeight,
                    renderAnnot,
                    viewBgColor,
                    pageBgColor
                )
                return
            }

            val pageWidth = page.getPageWidthPixel()
            val pageHeight = page.getPageHeightPixel()
            val rect = RectF(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat())

            val matrix = Matrix()
            val scale = min(realWidth * 1f / pageWidth, realHeight * 1f / pageHeight)
            matrix.postScale(scale, scale)
            val dx = (realWidth - pageWidth * scale) / 2
            val dy = (realHeight - pageHeight * scale) / 2
            matrix.postTranslate(dx, dy)
            matrix.postTranslate(1f * startX, 1f * startY)
            matrix.mapRect(rect)

            page.renderPageToBitmap(
                bitmap,
                rect.left.toInt(),
                rect.top.toInt(),
                rect.width().toInt(),
                rect.height().toInt(),
                renderAnnot,
                viewBgColor,
                pageBgColor
            )
        }
    }

    override fun renderPageToSurface(
        pageIndex: Int,
        surface: Surface,
        width: Int,
        height: Int,
        renderAnnot: Boolean,
        autoSize: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        renderPageToSurface(
            pageIndex,
            surface,
            0,
            0,
            width,
            height,
            renderAnnot,
            autoSize,
            viewBgColor,
            pageBgColor
        )
    }

    override fun renderPageToSurface(
        pageIndex: Int,
        surface: Surface,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        renderAnnot: Boolean,
        autoSize: Boolean,
        viewBgColor: Long,
        pageBgColor: Long
    ) {
        synchronized(lock) {
            val page = findDocByPageIndex(pageIndex)
                ?.getPageInfo(pageIndex)
                ?: return

            val realWidth = endX - startX
            val realHeight = endY - startY

            if (!autoSize) {
                page.renderPageToSurface(
                    surface,
                    startX,
                    startY,
                    realWidth,
                    realHeight,
                    renderAnnot,
                    viewBgColor,
                    pageBgColor
                )
                return
            }

            val pageWidth = page.getPageWidthPixel()
            val pageHeight = page.getPageHeightPixel()
            val rect = RectF(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat())

            val matrix = Matrix()
            val scale = min(realWidth * 1f / pageWidth, realHeight * 1f / pageHeight)
            matrix.postScale(scale, scale)
            val dx = (realWidth - pageWidth * scale) / 2
            val dy = (realHeight - pageHeight * scale) / 2
            matrix.postTranslate(dx, dy)
            matrix.postTranslate(1f * startX, 1f * startY)
            matrix.mapRect(rect)

            page.renderPageToSurface(
                surface,
                rect.left.toInt(),
                rect.top.toInt(),
                rect.width().toInt(),
                rect.height().toInt(),
                renderAnnot,
                viewBgColor,
                pageBgColor
            )
        }
    }

}