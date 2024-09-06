package com.proxy.service.webview.info.web.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.webview.info.config.Config

/**
 * @author: cangHX
 * @data: 2024/8/5 14:18
 * @desc:
 */
class LifecycleObserverImpl(private val web: IWeb) : DefaultLifecycleObserver {
    private val tag = "${Config.LOG_TAG_START}Lifecycle"

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        CsLogger.tag(tag).d("onResume")
        try {
            web.onResume()
//            web.resumeTimers()
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        CsLogger.tag(tag).d("onPause")
        try {
            web.onPause()
//            web.pauseTimers()
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        CsLogger.tag(tag).d("onDestroy")
        try {
            web.destroy()
        } catch (throwable: Throwable) {
            CsLogger.e(throwable)
        }
    }

}