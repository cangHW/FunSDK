package com.proxy.service.document.pdf.base.loader

import android.graphics.Bitmap
import android.view.Surface
import com.proxy.service.document.pdf.base.constants.PdfConstants

/**
 * @author: cangHX
 * @data: 2025/4/30 14:52
 * @desc:
 */
interface IPdfRender : IPdfAction {

    /**
     * 渲染页面到 bitmap
     *
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param autoSize      渲染尺寸自适应, 用于自动调整渲染内容的宽高, 避免最终渲染效果形变
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    fun renderPageToBitmap(
        pageIndex: Int,
        bitmap: Bitmap,
        renderAnnot: Boolean,
        autoSize: Boolean = false,
        viewBgColor:Long = PdfConstants.DEFAULT_BG_COLOR,
        pageBgColor:Long = PdfConstants.DEFAULT_BG_COLOR
    )

    /**
     * 渲染页面到 bitmap
     *
     * @param width         渲染区域的宽度
     * @param height        渲染区域的高度
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param autoSize      渲染尺寸自适应, 用于自动调整渲染内容的宽高, 避免最终渲染效果形变
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    fun renderPageToBitmap(
        pageIndex: Int,
        bitmap: Bitmap,
        width: Int,
        height: Int,
        renderAnnot: Boolean,
        autoSize: Boolean = false,
        viewBgColor:Long = PdfConstants.DEFAULT_BG_COLOR,
        pageBgColor:Long = PdfConstants.DEFAULT_BG_COLOR
    )

    /**
     * 渲染页面到 bitmap
     *
     * @param startX        渲染区域的开始坐标, 相对于 bitmap 左上角的 x 轴位置
     * @param startY        渲染区域的开始坐标, 相对于 bitmap 左上角的 y 轴位置
     * @param endX          渲染区域的结束坐标
     * @param endY          渲染区域的结束坐标
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param autoSize      渲染尺寸自适应, 用于自动调整渲染内容的宽高, 避免最终渲染效果形变
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    fun renderPageToBitmap(
        pageIndex: Int,
        bitmap: Bitmap,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        renderAnnot: Boolean,
        autoSize: Boolean = false,
        viewBgColor:Long = PdfConstants.DEFAULT_BG_COLOR,
        pageBgColor:Long = PdfConstants.DEFAULT_BG_COLOR
    )

    /**
     * 渲染页面到 Surface
     *
     * @param width         渲染区域的宽度
     * @param height        渲染区域的高度
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param autoSize      渲染尺寸自适应, 用于自动调整渲染内容的宽高, 避免最终渲染效果形变
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    fun renderPageToSurface(
        pageIndex: Int,
        surface: Surface,
        width: Int,
        height: Int,
        renderAnnot: Boolean,
        autoSize: Boolean = false,
        viewBgColor:Long = PdfConstants.DEFAULT_BG_COLOR,
        pageBgColor:Long = PdfConstants.DEFAULT_BG_COLOR
    )

    /**
     * 渲染页面到 Surface
     *
     * @param startX        渲染区域的开始坐标, 相对于 surface 左上角的 x 轴位置
     * @param startY        渲染区域的开始坐标, 相对于 surface 左上角的 y 轴位置
     * @param endX          渲染区域的结束坐标
     * @param endY          渲染区域的结束坐标
     * @param renderAnnot   是否渲染批注（高亮、下划线、便签等）
     * @param autoSize      渲染尺寸自适应, 用于自动调整渲染内容的宽高, 避免最终渲染效果形变
     * @param viewBgColor   视图背景色, 格式为：0xAARRGGBB
     * @param pageBgColor   页面背景色, 格式为：0xAARRGGBB
     * */
    fun renderPageToSurface(
        pageIndex: Int,
        surface: Surface,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int,
        renderAnnot: Boolean,
        autoSize: Boolean = false,
        viewBgColor:Long = PdfConstants.DEFAULT_BG_COLOR,
        pageBgColor:Long = PdfConstants.DEFAULT_BG_COLOR
    )

}