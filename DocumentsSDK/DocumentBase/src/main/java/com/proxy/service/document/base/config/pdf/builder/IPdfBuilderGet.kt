package com.proxy.service.document.base.config.pdf.builder

import com.proxy.service.document.base.config.pdf.callback.LoadStateCallback
import com.proxy.service.document.base.config.pdf.source.BaseSource

/**
 * @author: cangHX
 * @data: 2025/4/29 22:05
 * @desc:
 */
interface IPdfBuilderGet {

    fun getSources(): ArrayList<BaseSource>

    fun getLoadStateCallback(): LoadStateCallback?
}