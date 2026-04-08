package com.proxy.service.core.framework.ui.view.action.click

import android.view.View
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.ui.constants.UiViewConstants
import com.proxy.service.core.framework.ui.view.action.base.IViewActionCallback

/**
 * @author: cangHX
 * @data: 2026/1/8 17:03
 * @desc:
 */
class ClickActionImpl(private val view: View?) : IClickAction() {

    private var lastClickTime: Long = 0

    /**
     * 配置回调
     * */
    override fun call(callback: IViewActionCallback<Unit>) {
        if (view == null) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).w("The current view is null.")
            return
        }
        view.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > debounceTime) {
                lastClickTime = currentTime
                try {
                    callback.onViewActionCall(Unit)
                } catch (throwable: Throwable) {
                    if (isSafeMode) {
                        CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
                    } else {
                        throw throwable
                    }
                }
            } else {
                CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION)
                    .w("The current view clicks too quickly. debounceTime=${debounceTime}ms, view=$view")
            }
        }
    }
}