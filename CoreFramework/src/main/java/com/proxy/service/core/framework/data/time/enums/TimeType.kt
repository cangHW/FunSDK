package com.proxy.service.core.framework.data.time.enums

import java.util.Calendar

/**
 * @author: cangHX
 * @data: 2025/4/14 10:38
 * @desc:
 */
enum class TimeType {

    /**
     * 年
     * */
    YEAR {
        override fun update(calendar: Calendar, time: Int) {
            calendar.add(Calendar.YEAR, time)
        }
    },

    /**
     * 月
     * */
    MONTH {
        override fun update(calendar: Calendar, time: Int) {
            calendar.add(Calendar.MONTH, time)
        }
    },

    /**
     * 日
     * */
    DAY {
        override fun update(calendar: Calendar, time: Int) {
            calendar.add(Calendar.DAY_OF_YEAR, time)
        }
    },

    /**
     * 时
     * */
    HOUR {
        override fun update(calendar: Calendar, time: Int) {
            calendar.add(Calendar.HOUR, time)
        }
    },

    /**
     * 分
     * */
    MINUTE {
        override fun update(calendar: Calendar, time: Int) {
            calendar.add(Calendar.MINUTE, time)
        }
    },

    /**
     * 秒
     * */
    SECOND {
        override fun update(calendar: Calendar, time: Int) {
            calendar.add(Calendar.SECOND, time)
        }
    };

    abstract fun update(calendar: Calendar, time: Int)

}