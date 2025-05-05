package com.proxy.service.document.base

import com.proxy.service.base.BaseService
import com.proxy.service.document.base.config.pdf.PdfConfig
import com.proxy.service.document.base.pdf.IPdfLoader

/**
 * @author: cangHX
 * @data: 2025/4/29 21:33
 * @desc:
 */
interface PdfService : BaseService {

    /**
     * 创建 pdf 加载器，如果需要自定义 view 则通过加载器控制
     * */
    fun createLoader(config: PdfConfig): IPdfLoader

}