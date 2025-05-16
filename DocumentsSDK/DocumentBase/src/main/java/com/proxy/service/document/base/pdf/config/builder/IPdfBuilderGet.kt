package com.proxy.service.document.base.pdf.config.builder

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.document.base.pdf.config.callback.LoadStateCallback
import com.proxy.service.document.base.pdf.config.source.BaseSource

/**
 * @author: cangHX
 * @data: 2025/4/29 22:05
 * @desc:
 */
interface IPdfBuilderGet {

    fun getSources(): ArrayList<BaseSource>

    fun getLoadStateCallback(): LoadStateCallback?
}