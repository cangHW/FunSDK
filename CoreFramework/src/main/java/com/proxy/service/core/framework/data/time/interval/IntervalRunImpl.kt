package com.proxy.service.core.framework.data.time.interval

import com.proxy.service.core.framework.data.time.enums.TimeIntervalFormat
import com.proxy.service.core.framework.data.time.interval.base.IIntervalRun
import com.proxy.service.core.framework.data.time.interval.info.TimeIntervalInfo
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/4/15 11:42
 * @desc:
 */
open class IntervalRunImpl : IIntervalRun {

    companion object {
        private val formatMap = HashMap<String, TimeIntervalInfo>()
    }

    protected var timeMillis: Long = 0

    override fun get(format: TimeIntervalFormat): String {
        return get(format.pattern)
    }

    override fun get(pattern: String): String {
        if (timeMillis < 0) {
            return ""
        }

        return format(pattern)
    }

    private fun format(pattern: String): String {
        var intervalInfo = formatMap.get(pattern)
        if (intervalInfo == null) {
            intervalInfo = parseTimeInterval(pattern)
            formatMap.put(pattern, intervalInfo)
        }

        var time = timeMillis
        val builder = StringBuilder("")

        intervalInfo.dd?.let {
            val days = TimeUnit.MILLISECONDS.toDays(time)
            time -= TimeUnit.DAYS.toMillis(days)

            if (!pattern.startsWith("F-") || days > 0) {
                builder.append(days).append(it)
            }
        }

        intervalInfo.hh?.let {
            val hours = TimeUnit.MILLISECONDS.toHours(time)
            time -= TimeUnit.HOURS.toMillis(hours)

            if (!pattern.startsWith("F-") || hours > 0) {
                builder.append(complement(hours, intervalInfo.hLength)).append(it)
            }
        }

        intervalInfo.mm?.let {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
            time -= TimeUnit.MINUTES.toMillis(minutes)

            if (!pattern.startsWith("F-") || minutes > 0) {
                builder.append(complement(minutes, intervalInfo.mLength)).append(it)
            }
        }

        intervalInfo.ss?.let {
            val seconds = TimeUnit.MILLISECONDS.toSeconds(time)
            time -= TimeUnit.SECONDS.toMillis(seconds)

            if (!pattern.startsWith("F-") || seconds > 0) {
                builder.append(complement(seconds, intervalInfo.sLength)).append(it)
            }
        }

        intervalInfo.sss?.let {
            if (!pattern.startsWith("F-") || time > 0) {
                builder.append(time).append(it)
            }
        }

        if (builder.isEmpty() && pattern.startsWith("F-+")) {
            if (intervalInfo.sss != null) {
                builder.append(0).append(intervalInfo.sss)
            } else if (intervalInfo.ss != null) {
                builder.append(0).append(intervalInfo.ss)
            } else if (intervalInfo.mm != null) {
                builder.append(0).append(intervalInfo.mm)
            } else if (intervalInfo.hh != null) {
                builder.append(0).append(intervalInfo.hh)
            } else if (intervalInfo.dd != null) {
                builder.append(0).append(intervalInfo.dd)
            }
        }

        return builder.toString()
    }

    private fun parseTimeInterval(pattern: String): TimeIntervalInfo {
        val sorts: ArrayList<TimeIndexType> = ArrayList()

        val dIndex: Int = pattern.indexOf("DD")
        sorts.add(TimeIndexType(TimeIndexType.DAY, dIndex, 2))

        parseTimeWithHour(sorts, pattern)
        parseTimeWithMinutes(sorts, pattern)
        parseTimeWithSeconds(sorts, pattern)

        val msIndex: Int = pattern.indexOf("SSS")
        sorts.add(TimeIndexType(TimeIndexType.MILLIS, msIndex, 3))

        sorts.sortWith(object : Comparator<TimeIndexType?> {
            override fun compare(o1: TimeIndexType?, o2: TimeIndexType?): Int {
                if ((o1?.index ?: 0) > (o2?.index ?: 0)) {
                    return 1
                } else if ((o1?.index ?: 0) < (o2?.index ?: 0)) {
                    return -1
                }
                return 0
            }
        })

        val intervalInfo = TimeIntervalInfo()

        for (i in sorts.indices) {
            val type: TimeIndexType = sorts.get(i)
            if (type.index < 0) {
                continue
            }
            if (i == sorts.size - 1) {
                setTypeNameToInfo(
                    intervalInfo,
                    type,
                    pattern.substring(type.index + type.count),
                    type.count
                )
            } else {
                val type2: TimeIndexType = sorts.get(i + 1)
                setTypeNameToInfo(
                    intervalInfo,
                    type,
                    pattern.substring(type.index + type.count, type2.index),
                    type.count
                )
            }
        }

        return intervalInfo
    }

    private fun parseTimeWithHour(sorts: ArrayList<TimeIndexType>, pattern: String) {
        var hIndex: Int = pattern.indexOf("HH")
        if (hIndex >= 0) {
            sorts.add(TimeIndexType(TimeIndexType.HOURS, hIndex, 2))
            return
        }

        hIndex = pattern.indexOf("H")
        if (hIndex >= 0) {
            sorts.add(TimeIndexType(TimeIndexType.HOURS, hIndex, 1))
            return
        }
    }

    private fun parseTimeWithMinutes(sorts: ArrayList<TimeIndexType>, pattern: String) {
        var mIndex: Int = pattern.indexOf("MM")
        if (mIndex >= 0) {
            sorts.add(TimeIndexType(TimeIndexType.MINUTES, mIndex, 2))
            return
        }

        mIndex = pattern.indexOf("M")
        if (mIndex >= 0) {
            sorts.add(TimeIndexType(TimeIndexType.MINUTES, mIndex, 1))
        }
    }

    private fun parseTimeWithSeconds(sorts: ArrayList<TimeIndexType>, pattern: String) {
        var sIndex: Int = pattern.indexOf("SS")
        if (sIndex >= 0) {
            sorts.add(TimeIndexType(TimeIndexType.SECONDS, sIndex, 2))
            return
        }

        sIndex = pattern.indexOf("S")
        if (sIndex >= 0) {
            sorts.add(TimeIndexType(TimeIndexType.SECONDS, sIndex, 1))
        }
    }

    private fun setTypeNameToInfo(
        intervalInfo: TimeIntervalInfo,
        typeInfo: TimeIndexType,
        typeName: String,
        count: Int
    ) {
        when (typeInfo.name) {
            TimeIndexType.DAY -> {
                intervalInfo.dd = typeName
            }

            TimeIndexType.HOURS -> {
                intervalInfo.hh = typeName
                intervalInfo.hLength = count
            }

            TimeIndexType.MINUTES -> {
                intervalInfo.mm = typeName
                intervalInfo.mLength = count
            }

            TimeIndexType.SECONDS -> {
                intervalInfo.ss = typeName
                intervalInfo.sLength = count
            }

            TimeIndexType.MILLIS -> {
                intervalInfo.sss = typeName
            }
        }
    }

    private class TimeIndexType(val name: String, val index: Int, val count: Int) {

        companion object {
            const val DAY = "day"
            const val HOURS = "hours"
            const val MINUTES = "minutes"
            const val SECONDS = "seconds"
            const val MILLIS = "millis"
        }

        override fun toString(): String {
            return "TimeIndexType(name='$name', index=$index, count=$count)"
        }

    }

    private fun complement(num: Long, length: Int): String {
        var str = num.toString()
        while (str.length < length) {
            str = "0$str"
        }
        return str
    }
}