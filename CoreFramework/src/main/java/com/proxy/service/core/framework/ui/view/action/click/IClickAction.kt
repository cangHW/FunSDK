package com.proxy.service.core.framework.ui.view.action.click

import com.proxy.service.core.framework.ui.view.action.base.IAction
import com.proxy.service.core.framework.ui.view.action.base.ICall
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/1/8 17:01
 * @desc:
 */
abstract class IClickAction : IAction<IClickAction>(), ICall<Unit, Unit> {

    /**
     * 防抖间隔
     * */
    protected var debounceTime: Long = 0

    /**
     * 配置防抖间隔, 用于限制快速重复操作
     * */
    fun setDebounceTime(time: Long, unit: TimeUnit): IClickAction {
        debounceTime = unit.toMillis(time)
        return this
    }

}