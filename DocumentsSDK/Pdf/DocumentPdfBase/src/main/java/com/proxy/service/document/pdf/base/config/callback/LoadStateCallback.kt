package com.proxy.service.document.pdf.base.config.callback

import com.proxy.service.document.pdf.base.config.info.FailedResult
import com.proxy.service.document.pdf.base.config.source.BaseSource

/**
 * @author: cangHX
 * @data: 2025/5/5 13:56
 * @desc:
 */
interface LoadStateCallback {

    /**
     * 开始加载
     * */
    fun onLoadStart(sources: List<BaseSource>) {}

    /**
     * 结束加载
     * */
    fun onLoadComplete(success: List<BaseSource>, failed: List<FailedResult>)
}