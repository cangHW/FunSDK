package com.proxy.service.core.framework.data.time.total

import android.annotation.SuppressLint
import android.os.Build
import com.proxy.service.core.framework.data.time.enums.TimeFormat
import com.proxy.service.core.framework.data.time.total.base.ITotalRun
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


/**
 * @author: cangHX
 * @data: 2025/4/14 18:22
 * @desc:
 */
open class TotalRunImpl : ITotalRun {

    protected var calendar:Calendar? = null

    override fun get(format: TimeFormat): String {
        return get(format.pattern)
    }

    @SuppressLint("SimpleDateFormat")
    override fun get(pattern: String): String {
        val time = calendar?.timeInMillis ?: -1

        if (time <= 0) {
            return ""
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(time),
                ZoneId.systemDefault()
            )
            val formatter = DateTimeFormatter.ofPattern(pattern)
            return dateTime.format(formatter)
        }

        val date = Date(time)
        val formatter = SimpleDateFormat(pattern)
        return formatter.format(date)
    }

}