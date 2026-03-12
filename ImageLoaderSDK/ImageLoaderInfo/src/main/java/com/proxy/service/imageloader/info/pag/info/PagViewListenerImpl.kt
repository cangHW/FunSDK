package com.proxy.service.imageloader.info.pag.info

import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
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
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationStartCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationEnd(p0: PAGView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationEndCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationCancel(p0: PAGView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationCancelCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationRepeat(p0: PAGView?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                info.animationRepeatCallback?.onAnimation()
                return ""
            }
        })?.start()
    }

    override fun onAnimationUpdate(p0: PAGView?) {
        info.animationUpdateCallback?.onUpdate(p0?.progress ?: 0.0)
    }
}