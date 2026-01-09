package com.proxy.service.core.framework.ui.view.action.longclick

import android.view.View
import android.view.View.OnLongClickListener
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.ui.constants.UiViewConstants
import com.proxy.service.core.framework.ui.view.action.base.IViewActionCallback

/**
 * @author: cangHX
 * @data: 2026/1/8 20:51
 * @desc:
 */
class LongClickActionImpl(private val view: View?) : ILongClickAction() {

    /**
     * 配置回调, 不拦截系统默认行为
     * */
    override fun callOnly(callback: IViewActionCallback<Unit>) {
        if (view == null) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).w("The current view is null.")
            return
        }
        view.setOnLongClickListener(createListener(false, callback))
    }

    /**
     * 配置回调
     * */
    override fun call(callback: IViewActionCallback<Unit>) {
        if (view == null) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).w("The current view is null.")
            return
        }
        view.setOnLongClickListener(createListener(true, callback))
    }

    private fun createListener(
        isIntercept: Boolean,
        callback: IViewActionCallback<Unit>
    ): OnLongClickListener {
        return OnLongClickListener {
            try {
                callback.onViewActionCall(Unit)
            } catch (throwable: Throwable) {
                if (isSafeMode) {
                    CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).e(throwable)
                } else {
                    throw throwable
                }
            }
            isIntercept
        }
    }
}