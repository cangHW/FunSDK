package com.proxy.service.core.framework.ui.constants

import com.proxy.service.core.constants.CoreConfig

/**
 * @author: cangHX
 * @data: 2026/1/8 18:43
 * @desc:
 */
object UiViewConstants {

    private const val TAG_PREFIX = "${CoreConfig.TAG}Ui_"

    const val TAG_VIEW_ACTION = "${TAG_PREFIX}ViewAction"





    /**
     * 显示状态检查任务
     * */
    const val EXPOSURE_LOOP_NAME_FOR_CHECK = "exposure-monitor-task"

    /**
     * 显示状态回调任务
     * */
    const val EXPOSURE_LOOP_NAME_FOR_CALL = "visible-monitor-call-task"

    /**
     * 默认曝光的有效区域比例
     * */
    const val EXPOSURE_DEFAULT_AREA: Float = 0.5f

    /**
     * 默认曝光的有效时长
     * */
    const val EXPOSURE_DEFAULT_DURATION: Long = 1000

    /**
     * 默认曝光的检测间隔时间
     * */
    const val EXPOSURE_DEFAULT_DELAY_MILLIS: Long = 500
}