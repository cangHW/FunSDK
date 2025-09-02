package com.proxy.service.document.pdf.info.view.view.show

import android.view.ViewGroup
import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.callback.OnShouldCreateCoverViewCallback
import com.proxy.service.document.pdf.info.view.cache.PartCache

/**
 * @author: cangHX
 * @data: 2025/7/21 18:47
 * @desc:
 */
abstract class BaseShow {

    interface OnPageShowCallback {
        /**
         * 页面显示回调
         * */
        fun onPageShow(position: Int)
    }

    protected var pageShowCallback: OnPageShowCallback? = null
    protected var createCoverViewCallback: OnShouldCreateCoverViewCallback? = null
    protected var maxShowCount: Int = PdfConstants.DEFAULT_PAGE_SHOW_MAX_COUNT

    fun setOnPageShowCallback(callback: OnPageShowCallback) {
        this.pageShowCallback = callback
    }

    fun setOnCreateCoverViewCallback(callback: OnShouldCreateCoverViewCallback) {
        this.createCoverViewCallback = callback
    }

    fun setPageShowMaxCount(maxCount: Int){
        this.maxShowCount = maxCount
    }

    fun destroy() {
        pageShowCallback = null
        createCoverViewCallback = null
    }

    abstract fun setViewGroup(viewGroup: ViewGroup)

    abstract fun show(loader: IPdfLoader, cache: PartCache, showIndex: Int)

}