package com.proxy.service.core.framework.data.time.total.base

import com.proxy.service.core.framework.data.time.enums.TimeType

/**
 * @author: cangHX
 * @data: 2025/4/14 10:35
 * @desc:
 */
interface ITotalOption : ITotalRun {

    /**
     * 增加时间
     * */
    fun addTime(value: Int, type: TimeType): ITotalOption

    /**
     * 减少时间
     * */
    fun subtractTime(value: Int, type: TimeType): ITotalOption
}