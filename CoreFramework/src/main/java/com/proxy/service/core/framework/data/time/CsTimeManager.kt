package com.proxy.service.core.framework.data.time

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.interval.IntervalOptionImpl
import com.proxy.service.core.framework.data.time.interval.base.IIntervalOption
import com.proxy.service.core.framework.data.time.total.TotalOptionImpl
import com.proxy.service.core.framework.data.time.total.base.ITotalOption

/**
 * @author: cangHX
 * @data: 2025/4/14 10:32
 * @desc:
 */
object CsTimeManager {

    private const val TAG = "${CoreConfig.TAG}TimeManager"

    private const val MAX_LENGTH = 13

    private fun formatNum(timeMillis: Long): Long {
        val numberStr: String = java.lang.String.valueOf(timeMillis)
        if (numberStr.length == MAX_LENGTH) {
            return timeMillis
        }
        val zerosToAdd = MAX_LENGTH - numberStr.length
        if (zerosToAdd > 0) {
            val sb = StringBuilder(numberStr)
            for (i in 0 until zerosToAdd) {
                sb.append('0')
            }
            return sb.toString().toLong()
        }
        CsLogger.tag(TAG).d("timeMillis length is too long, timeMillis = $timeMillis")
        return 0
    }

    /**
     * 创建日期处理器
     * */
    fun createFactory(timeMillis: Long = System.currentTimeMillis()): ITotalOption {
        val option = TotalOptionImpl()
        formatNum(timeMillis).let {
            if (it > 0) {
                option.setTime(it)
            }
        }
        return option
    }

    /**
     * 创建间隔时间处理器
     * */
    fun createIntervalFactory(intervalMillis: Long): IIntervalOption {
        val option = IntervalOptionImpl()
        if (intervalMillis >= 0) {
            option.setTime(intervalMillis)
        }
        return option
    }

}