package com.proxy.service.imageloader.info.pag.info

import org.libpag.PAGView

/**
 * @author: cangHX
 * @data: 2025/10/10 18:56
 * @desc:
 */
class PagViewListenerImpl(
    private val info: PagInfo
) : PAGView.PAGViewListener {

    override fun onAnimationStart(p0: PAGView?) {
        info.animationStartCallback?.onAnimation()
    }

    override fun onAnimationEnd(p0: PAGView?) {
        info.animationEndCallback?.onAnimation()
    }

    override fun onAnimationCancel(p0: PAGView?) {
        info.animationCancelCallback?.onAnimation()
    }

    override fun onAnimationRepeat(p0: PAGView?) {
        info.animationRepeatCallback?.onAnimation()
    }

    override fun onAnimationUpdate(p0: PAGView?) {
        info.animationUpdateCallback?.onUpdate(p0?.progress ?: 0.0)
    }
}