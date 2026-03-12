package com.proxy.service.imageloader.info.pag.info

import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import org.libpag.PAGImageView

/**
 * @author: cangHX
 * @data: 2025/10/10 18:56
 * @desc:
 */
class PagImageViewListenerImpl(
    private val info: PagInfo
) : PAGImageView.PAGImageViewListener {

    override fun onAnimationStart(p0: PAGImageView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationStartCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationEnd(p0: PAGImageView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationEndCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationCancel(p0: PAGImageView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationCancelCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationRepeat(p0: PAGImageView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationRepeatCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationUpdate(p0: PAGImageView?) {
        if (p0 == null) {
            info.animationUpdateCallback?.onUpdate(0.0)
            return
        }
        val total = p0.numFrames()
        if (total <= 0) {
            info.animationUpdateCallback?.onUpdate(0.0)
            return
        }
        info.animationUpdateCallback?.onUpdate(((p0.currentFrame() + 1) * 1.0) / total)
    }
}