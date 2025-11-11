package com.proxy.service.imageloader.info.pag.loader.view

import android.content.Context
import android.util.AttributeSet
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.info.pag.info.PagImageViewListenerImpl
import com.proxy.service.imageloader.info.pag.info.PagInfo
import org.libpag.PAGComposition
import org.libpag.PAGImageView

/**
 * @author: cangHX
 * @data: 2025/11/11 16:01
 * @desc:
 */
class PagImageViewImpl: PAGImageView, IView {

    private var pagListener: PagImageViewListenerImpl? = null

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
        pagListener = PagImageViewListenerImpl(pagInfo)
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
        setCurrentFrame((numFrames() * progress).toInt())
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

}