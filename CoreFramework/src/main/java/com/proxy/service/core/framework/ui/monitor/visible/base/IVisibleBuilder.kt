package com.proxy.service.core.framework.ui.monitor.visible.base

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.ui.monitor.visible.callback.VisibleMonitorCallback
import com.proxy.service.core.framework.ui.monitor.visible.config.VisibleMonitorConfig
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/4 18:35
 * @desc:
 */
interface IVisibleBuilder {

    /**
     * 设置生命周期管理, 可用于自动关闭监控
     * */
    fun setLifecycle(lifecycleOwner: LifecycleOwner): IVisibleBuilder

    /**
     * 设置曝光的有效区域比例（0—1 百分比）,展示多少算一次有效曝光，默认为 0.5
     *
     * @param area  有效区域比例（0—1 百分比）
     */
    fun setArea(area: Float): IVisibleBuilder

    /**
     * 设置曝光的有效时长, 多久算一次有效曝光, 默认为 1000ms
     *
     * @param duration  曝光的有效时长
     * @param unit      时间格式
     */
    fun setDuration(duration: Long, unit: TimeUnit): IVisibleBuilder

    /**
     * 设置曝光的检测间隔时间，时间越短, 灵敏度越高，默认为 500ms
     *
     * @param delayMillis   曝光的检测间隔时间
     * @param unit          时间格式
     */
    fun setDelayMillis(delayMillis: Long, unit: TimeUnit): IVisibleBuilder

    /**
     * 设置标记
     *
     * @param tag   自定义对象
     * */
    fun setTag(tag: Any): IVisibleBuilder

    /**
     * 创建配置
     */
    fun build(): VisibleMonitorConfig

}