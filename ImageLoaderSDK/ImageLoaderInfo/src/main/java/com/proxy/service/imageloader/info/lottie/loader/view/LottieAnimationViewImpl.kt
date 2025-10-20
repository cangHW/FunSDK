package com.proxy.service.imageloader.info.lottie.loader.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieDrawable
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants

/**
 * @author: cangHX
 * @data: 2025/9/23 11:11
 * @desc:
 */
class LottieAnimationViewImpl : LottieAnimationView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    interface OnDrawErrorCallback {
        fun onErrorCallback(throwable: Throwable)
    }

    private var drawErrorCallback: OnDrawErrorCallback? = null

    fun setOnDrawErrorCallback(callback: OnDrawErrorCallback) {
        this.drawErrorCallback = callback
    }

    override fun setComposition(composition: LottieComposition) {
        resetLottieDrawable()
        super.setComposition(composition)
    }

    override fun onDraw(canvas: Canvas) {
        try {
            super.onDraw(canvas)
        } catch (throwable: Throwable) {
            CsLogger.tag(ImageLoaderConstants.TAG).e(throwable)
            this.drawErrorCallback?.onErrorCallback(throwable)
        }
    }

    private fun resetLottieDrawable(){
        val d = getDrawable()
        if (d is LottieDrawable){
            d.clearComposition()
        }
    }
}