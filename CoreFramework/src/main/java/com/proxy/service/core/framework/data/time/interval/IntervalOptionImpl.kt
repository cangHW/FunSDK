package com.proxy.service.core.framework.data.time.interval

import com.proxy.service.core.framework.data.time.interval.base.IIntervalOption

/**
 * @author: cangHX
 * @data: 2025/4/15 11:25
 * @desc:
 */
class IntervalOptionImpl : IntervalRunImpl(), IIntervalOption {

    fun setTime(time: Long) {
        this.timeMillis = time
    }

}