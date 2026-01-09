package com.proxy.service.core.framework.ui.view.action.exposure

import android.view.View
import com.proxy.service.core.R
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.ui.constants.UiViewConstants
import com.proxy.service.core.framework.ui.view.action.base.IViewActionCallback

/**
 * @author: cangHX
 * @data: 2026/1/9 14:55
 * @desc:
 */
class ExposureActionImpl(private val view: View?) : IExposureAction() {

    /**
     * 配置回调
     * */
    override fun call(callback: IViewActionCallback<ExposureParams>): ExposureController? {
        if (view == null) {
            CsLogger.tag(UiViewConstants.TAG_VIEW_ACTION).w("The current view is null.")
            return null
        }

        val oldController = view.getTag(R.id.cs_core_fw_ui_view_action_tag)
        if (oldController is ExposureControllerImpl) {
            oldController.stop()
            oldController.release()
            view.removeOnAttachStateChangeListener(oldController)
        }


//        view.addOnAttachStateChangeListener()


        return null
    }
}