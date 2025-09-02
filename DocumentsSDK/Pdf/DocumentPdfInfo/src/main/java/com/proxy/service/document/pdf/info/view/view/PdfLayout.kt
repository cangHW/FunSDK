package com.proxy.service.document.pdf.info.view.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.pdf.base.constants.PdfConstants
import com.proxy.service.document.pdf.base.enums.ViewShowType
import com.proxy.service.document.pdf.base.loader.IPdfLoader
import com.proxy.service.document.pdf.base.view.callback.OnShouldCreateCoverViewCallback
import com.proxy.service.document.pdf.info.view.cache.PartCache
import com.proxy.service.document.pdf.info.view.view.show.BaseShow
import com.proxy.service.document.pdf.info.view.view.show.MultiPageHorizontal
import com.proxy.service.document.pdf.info.view.view.show.MultiPageVertical
import com.proxy.service.document.pdf.info.view.view.show.SinglePageHorizontal
import com.proxy.service.document.pdf.info.view.view.show.SinglePageVertical
import com.proxy.service.document.pdf.info.view.view.show.TwoPageLandscape

/**
 * @author: cangHX
 * @data: 2025/5/9 09:54
 * @desc:
 */
class PdfLayout : FrameLayout, BaseShow.OnPageShowCallback, OnShouldCreateCoverViewCallback {

    companion object{
        private const val TAG = "${PdfConstants.LOG_TAG_PDF_START}PdfLayout"
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var baseShow: BaseShow? = null
    private var currentShowPage: Int = 0
    private var maxShowCount: Int = PdfConstants.DEFAULT_PAGE_SHOW_MAX_COUNT

    private var createCoverViewCallback: OnShouldCreateCoverViewCallback? = null

    fun setPageShowMaxCount(maxCount: Int){
        this.maxShowCount = maxCount
    }

    fun setShowType(type: ViewShowType) {
        baseShow?.destroy()

        when (type) {
            ViewShowType.MULTI_PAGE_VERTICAL -> {
                baseShow = MultiPageVertical()
            }
            ViewShowType.MULTI_PAGE_HORIZONTAL -> {
                baseShow = MultiPageHorizontal()
            }
            ViewShowType.SINGLE_PAGE_VERTICAL -> {
                baseShow = SinglePageVertical()
            }
            ViewShowType.SINGLE_PAGE_HORIZONTAL -> {
                baseShow = SinglePageHorizontal()
            }
            ViewShowType.TWO_PAGE_TO_ONE_LANDSCAPE -> {
                baseShow = TwoPageLandscape()
            }
        }

        baseShow?.setOnCreateCoverViewCallback(this)
        baseShow?.setOnPageShowCallback(this)
        baseShow?.setPageShowMaxCount(maxShowCount)
        baseShow?.setViewGroup(this)
    }

    fun setOnCreateCoverViewCallback(callback: OnShouldCreateCoverViewCallback?) {
        this.createCoverViewCallback = callback
    }

    fun setLoader(loader: IPdfLoader, cache: PartCache) {
        baseShow?.show(loader, cache, currentShowPage)
    }

    override fun onPageShow(position: Int) {
        this.currentShowPage = position

        CsLogger.tag(TAG).d("onPageShow position = $position")
    }

    override fun onShouldShowCoverView(position: Int, coverLayout: FrameLayout) {
        createCoverViewCallback?.onShouldShowCoverView(position, coverLayout)
    }
}