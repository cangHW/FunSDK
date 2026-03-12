package com.proxy.service.imageloader.info.pag.loader.view

import android.content.Context
import android.util.AttributeSet
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.info.pag.info.PagInfo
import com.proxy.service.imageloader.info.pag.info.PagViewListenerImpl
import org.libpag.PAGComposition
import org.libpag.PAGView

/**
 * @author: cangHX
 * @data: 2025/10/10 18:28
 * @desc:
 */
class PagViewImpl : PAGView, IView {

    private var pagListener: PagViewListenerImpl? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun checkListener(pagInfo: PagInfo) {
        pagListener?.let {
            try {
                removeListener(it)
            } catch (_: Throwable) {
            }
        }
        pagListener = PagViewListenerImpl(pagInfo)
        pagListener?.let {
            try {
                addListener(it)
            } catch (throwable: Throwable) {
                CsLogger.tag(ImageLoaderConstants.TAG).e(throwable)
            }
        }
    }

    override fun setPagComposition(composition: PAGComposition?) {
        setComposition(composition)
    }

    override fun setPagProgress(progress: Double) {
        setProgress(progress)
    }

    override fun setPagRepeatCount(count: Int) {
        setRepeatCount(count)
    }

    override fun playPag() {
        play()
    }

    override fun stopPag() {
        pause()
    }

    override fun releaseCache() {
        freeCache()
    }

}