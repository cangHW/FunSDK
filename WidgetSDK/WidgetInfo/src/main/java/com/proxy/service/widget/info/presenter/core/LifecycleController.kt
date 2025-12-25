package com.proxy.service.widget.info.presenter.core

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.presenter.constants.PresenterConstants

/**
 * @author: cangHX
 * @data: 2025/12/25 10:54
 * @desc:
 */
class LifecycleController(private var iLifecycle: ILifecycle?) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        try {
            iLifecycle?.onCreate()
        } catch (throwable: Throwable) {
            CsLogger.tag(PresenterConstants.TAG).e(throwable)
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        try {
            iLifecycle?.onStart()
        } catch (throwable: Throwable) {
            CsLogger.tag(PresenterConstants.TAG).e(throwable)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        try {
            iLifecycle?.onResume()
        } catch (throwable: Throwable) {
            CsLogger.tag(PresenterConstants.TAG).e(throwable)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        try {
            iLifecycle?.onPause()
        } catch (throwable: Throwable) {
            CsLogger.tag(PresenterConstants.TAG).e(throwable)
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        try {
            iLifecycle?.onStop()
        } catch (throwable: Throwable) {
            CsLogger.tag(PresenterConstants.TAG).e(throwable)
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        try {
            iLifecycle?.onDestroy()
        } catch (throwable: Throwable) {
            CsLogger.tag(PresenterConstants.TAG).e(throwable)
        } finally {
            iLifecycle = null
        }
    }
}