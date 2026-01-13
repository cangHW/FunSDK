package com.proxy.service.core.framework.ui.view.action

import android.view.View
import com.proxy.service.core.framework.ui.view.action.click.ClickActionImpl
import com.proxy.service.core.framework.ui.view.action.click.IClickAction
import com.proxy.service.core.framework.ui.view.action.exposure.action.ExposureActionImpl
import com.proxy.service.core.framework.ui.view.action.exposure.action.IExposureAction
import com.proxy.service.core.framework.ui.view.action.longclick.ILongClickAction
import com.proxy.service.core.framework.ui.view.action.longclick.LongClickActionImpl

/**
 * @author: cangHX
 * @data: 2026/1/8 16:40
 * @desc: view 行为事件绑定处理
 */
object CsViewAction {

    /**
     * 绑定 view 的点击事件
     * */
    fun click(view: View?): IClickAction {
        return ClickActionImpl(view)
    }

    /**
     * 绑定 view 的长按事件
     * */
    fun longClick(view: View?): ILongClickAction {
        return LongClickActionImpl(view)
    }

    /**
     * 绑定 view 的曝光事件
     * */
    fun exposure(view: View?): IExposureAction {
        return ExposureActionImpl(view)
    }

}