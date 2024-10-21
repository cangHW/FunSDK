package com.proxy.service.core.framework.data.time

import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/10/21 10:35
 * @desc:
 */
object CsTimeUtils {

    /**
     * 将间隔时间转化为：xx日xx小时xx分钟xx秒xx毫秒
     *
     * @param   duration    间隔时间
     * @param   timeUnit    时间格式
     * */
    fun toIntervalString(duration: Long, timeUnit: TimeUnit): String {
        var time = timeUnit.toMillis(duration)
        val builder = StringBuilder()

        val days = TimeUnit.MILLISECONDS.toDays(time)
        if (days > 0) {
            builder.append("$days").append("日")
            time -= TimeUnit.DAYS.toMillis(days)
        }

        val hours = TimeUnit.MILLISECONDS.toHours(time)
        if (hours > 0) {
            builder.append("$hours").append("小时")
            time -= TimeUnit.HOURS.toMillis(hours)
        }

        val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
        if (minutes > 0) {
            builder.append("$minutes").append("分钟")
            time -= TimeUnit.MINUTES.toMillis(minutes)
        }

        val seconds = TimeUnit.MILLISECONDS.toSeconds(time)
        if (seconds > 0) {
            builder.append("$seconds").append("秒")
            time -= TimeUnit.SECONDS.toMillis(seconds)
        }

        if (time > 0) {
            builder.append("$time").append("毫秒")
        }
        return builder.toString()
    }

}