package com.proxy.service.core.framework.ui.view.action.exposure

import com.proxy.service.core.framework.ui.constants.UiViewConstants
import com.proxy.service.core.framework.ui.view.action.base.IAction
import com.proxy.service.core.framework.ui.view.action.base.ICall
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/1/9 14:10
 * @desc:
 */
abstract class IExposureAction : IAction<IExposureAction>(), ICall<ExposureParams, ExposureController?> {

    protected var area: Float = UiViewConstants.EXPOSURE_DEFAULT_AREA
    protected var duration: Long = UiViewConstants.EXPOSURE_DEFAULT_DURATION
    protected var delayMillis: Long = UiViewConstants.EXPOSURE_DEFAULT_DELAY_MILLIS

    protected var tag: Any? = null

    /**
     * 设置曝光的有效区域比例（0—1 百分比）,展示多少算一次有效曝光，默认为 [UiViewConstants.EXPOSURE_DEFAULT_AREA]
     *
     * @param area  有效区域比例（0—1 百分比）
     */
    fun setArea(area: Float): IExposureAction {
        if (area > 0 && area <= 1) {
            this.area = area
        }
        return this
    }

    /**
     * 设置曝光的有效时长, 多久算一次有效曝光, 默认为 [UiViewConstants.EXPOSURE_DEFAULT_DURATION]
     *
     * @param duration  曝光的有效时长
     * @param unit      时间格式
     */
    fun setDuration(duration: Long, unit: TimeUnit): IExposureAction {
        unit.toMillis(duration).let {
            if (it >= 0) {
                this.duration = it
            } else {
                this.duration = 0
            }
        }
        return this
    }

    /**
     * 设置曝光的检测间隔时间，时间越短, 灵敏度越高，默认为 [UiViewConstants.EXPOSURE_DEFAULT_DELAY_MILLIS]
     *
     * @param delayMillis   曝光的检测间隔时间
     * @param unit          时间格式
     */
    fun setDelayMillis(delayMillis: Long, unit: TimeUnit): IExposureAction {
        unit.toMillis(delayMillis).let {
            if (it >= 10) {
                this.delayMillis = it
            } else {
                this.delayMillis = 10
            }
        }
        return this
    }

    /**
     * 设置标记
     *
     * @param tag   自定义对象, 可以用于埋点、记录等操作
     * */
    fun setTag(tag: Any): IExposureAction {
        this.tag = tag
        return this
    }

}