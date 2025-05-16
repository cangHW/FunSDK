package com.proxy.service.document.base

import android.view.ViewGroup
import com.proxy.service.base.BaseService
import com.proxy.service.document.base.pdf.config.PdfConfig
import com.proxy.service.document.base.pdf.loader.IPdfLoader
import com.proxy.service.document.base.pdf.view.IPdfView
import com.proxy.service.document.base.pdf.view.IViewFactory

/**
 * @author: cangHX
 * @data: 2025/4/29 21:33
 * @desc:
 */
interface PdfService : BaseService {

    /**
     * 创建 pdf 加载器，如果需要自定义 view 则通过加载器控制
     *
     * @param config    pdf 配置
     * */
    fun createLoader(config: PdfConfig): IPdfLoader

    /**
     * 创建 pdf 视图工厂, 如果需要使用默认视图
     *
     * @param config    pdf 配置
     * */
    fun createViewFactory(config: PdfConfig): IViewFactory
}