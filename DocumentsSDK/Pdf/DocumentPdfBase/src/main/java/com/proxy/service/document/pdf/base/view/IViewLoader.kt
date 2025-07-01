package com.proxy.service.document.pdf.base.view

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.enums.PagePixelFormat
import com.proxy.service.document.pdf.base.constants.PdfConstants

/**
 * @author: cangHX
 * @data: 2025/5/15 09:42
 * @desc:
 */
interface IViewLoader {

    /**
     * 设置视图背景色, 格式为：0xAARRGGBB, 默认: [PdfConstants.DEFAULT_BG_COLOR]
     * */
    fun setViewBackgroundColor(color: Long): IViewLoader

    /**
     * 设置页面背景色, 格式为：0xAARRGGBB, 默认: [PdfConstants.DEFAULT_BG_COLOR]
     * */
    fun setPageBackgroundColor(color: Long): IViewLoader

    /**
     * 设置页面最大缓存数量, 数量越大, 则体验越好, 但是占用内存越大, 默认: [PdfConstants.DEFAULT_PAGE_CACHE_MAX_COUNT].
     * 最小值为: [PdfConstants.PAGE_CACHE_MIN_COUNT],
     * 与 [setPageCacheMaxSize] 互斥
     * */
    fun setPageCacheMaxCount(maxCount: Int): IViewLoader

    /**
     * 设置页面最大缓存内存大小, 消耗内存越大, 体验越好, 无默认.
     * 最小值为: [PdfConstants.PAGE_CACHE_MIN_SIZE],
     * 与 [setPageCacheMaxCount] 互斥
     * */
    fun setPageCacheMaxSize(maxSize: Int): IViewLoader

    /**
     * 设置页面清晰度, 默认: [PagePixelFormat.ARGB_8888]
     * */
    fun setPagePixelFormat(format: PagePixelFormat): IViewLoader

    /**
     * 绑定生命周期
     * */
    fun setLifecycleOwner(owner: LifecycleOwner): IViewLoader

    /**
     * 设置加载回调
     * */
    fun setLoadStateCallback(callback: LoadStateCallback): IViewLoader

    /**
     * 创建控制器并加载到父视图
     * */
    fun into(viewGroup: ViewGroup): IPdfView
}