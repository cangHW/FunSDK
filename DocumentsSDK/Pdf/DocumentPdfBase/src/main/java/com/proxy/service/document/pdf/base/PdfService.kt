package com.proxy.service.document.pdf.base

import com.proxy.service.base.BaseService
import com.proxy.service.document.pdf.base.config.PdfConfig
import com.proxy.service.document.pdf.base.config.callback.LoadStateCallback
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.IViewFactory

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
     * @param callback  加载回调
     * */
    fun createLoader(config: PdfConfig, callback: LoadStateCallback): IPdfLoader

    /**
     * 创建 pdf 视图工厂, 如果需要使用默认视图
     *
     * @param config    pdf 配置
     * */
    fun createViewFactory(config: PdfConfig): IViewFactory
}