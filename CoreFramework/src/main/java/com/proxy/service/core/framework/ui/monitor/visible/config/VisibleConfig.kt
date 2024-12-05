package com.proxy.service.core.framework.ui.monitor.visible.config

import com.proxy.service.core.constants.Constants

/**
 * @author: cangHX
 * @data: 2024/12/4 20:40
 * @desc:
 */
object VisibleConfig {

    const val TAG = "${Constants.TAG}Monitor_V"

    /**
     * 显示状态检查任务
     * */
    const val LOOP_NAME_FOR_CHECK = "visible-monitor-check-task"

    /**
     * 显示状态回调任务
     * */
    const val LOOP_NAME_FOR_CALL = "visible-monitor-call-task"

    /**
     * 默认曝光的有效区域比例
     * */
    const val DEFAULT_AREA: Float = 0.5f

    /**
     * 默认曝光的有效时长
     * */
    const val DEFAULT_DURATION: Long = 1000

    /**
     * 默认曝光的检测间隔时间
     * */
    const val DEFAULT_DELAY_MILLIS: Long = 500


}