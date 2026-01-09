package com.proxy.service.core.framework.ui.view.action.longclick

import com.proxy.service.core.framework.ui.view.action.base.IAction
import com.proxy.service.core.framework.ui.view.action.base.ICall
import com.proxy.service.core.framework.ui.view.action.base.IViewActionCallback

/**
 * @author: cangHX
 * @data: 2026/1/8 20:54
 * @desc:
 */
abstract class ILongClickAction : IAction<ILongClickAction>(), ICall<Unit, Unit> {

    /**
     * 配置回调, 不拦截系统默认行为
     * */
    abstract fun callOnly(callback: IViewActionCallback<Unit>)

}