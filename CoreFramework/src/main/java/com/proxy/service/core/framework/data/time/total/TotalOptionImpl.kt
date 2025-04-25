package com.proxy.service.core.framework.data.time.total

import com.proxy.service.core.framework.data.time.enums.TimeType
import com.proxy.service.core.framework.data.time.total.base.ITotalOption
import java.util.Calendar
import java.util.Date

/**
 * @author: cangHX
 * @data: 2025/4/14 10:40
 * @desc:
 */
class TotalOptionImpl : TotalRunImpl(), ITotalOption {

    fun setTime(time: Long) {
        val date = Date(time)
        calendar = Calendar.getInstance()
        calendar?.setTime(date)
    }

    override fun addTime(value: Int, type: TimeType): ITotalOption {
        calendar?.let {
            type.update(it, value)
        }
        return this
    }

    override fun subtractTime(value: Int, type: TimeType): ITotalOption {
        calendar?.let {
            type.update(it, -value)
        }
        return this
    }

}