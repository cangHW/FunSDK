package com.proxy.service.core.framework.ui.monitor.visible.base

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.ui.monitor.visible.callback.VisibleMonitorCallback

/**
 * @author: cangHX
 * @data: 2024/12/4 18:36
 * @desc:
 */
interface IVisibleBuilderGet {

    /**
     * 获取绑定的 view
     * */
    fun getBindView(): View

    /**
     * 获取绑定的生命周期
     * */
    fun getLifecycleOwner(): LifecycleOwner?

    /**
     * 获取曝光的有效区域比例
     */
    fun getArea(): Float

    /**
     * 获取曝光的有效时长
     */
    fun getDuration(): Long

    /**
     * 获取曝光的检测间隔时间
     */
    fun getDelayMillis(): Long

    /**
     * 获取自定义标记对象
     * */
    fun getTag(): Any?

}