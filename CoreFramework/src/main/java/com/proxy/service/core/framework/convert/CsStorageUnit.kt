package com.proxy.service.core.framework.convert

import android.annotation.SuppressLint

/**
 * 存储数据格式转换器
 *
 * @author: cangHX
 * @data: 2024/7/26 11:38
 * @desc:
 */
enum class CsStorageUnit(private val type: String, private val unit: Int) {

    // 单位进制 1024

    GB_UNIT_1024(TYPE.TYPE_GB, TYPE.UNIT_1024),
    MB_UNIT_1024(TYPE.TYPE_MB, TYPE.UNIT_1024),
    KB_UNIT_1024(TYPE.TYPE_KB, TYPE.UNIT_1024),
    B_UNIT_1024(TYPE.TYPE_B, TYPE.UNIT_1024),

    // 单位进制 1000

    GB_UNIT_1000(TYPE.TYPE_GB, TYPE.UNIT_1000),
    MB_UNIT_1000(TYPE.TYPE_MB, TYPE.UNIT_1000),
    KB_UNIT_1000(TYPE.TYPE_KB, TYPE.UNIT_1000),
    B_UNIT_1000(TYPE.TYPE_B, TYPE.UNIT_1000);

    /**
     * 转化为 Gb, 格式为 long
     * */
    fun toGbLong(size: Long): Long {
        return when (type) {
            TYPE.TYPE_GB -> {
                size
            }

            TYPE.TYPE_MB -> {
                size / unit
            }

            TYPE.TYPE_KB -> {
                size / unit / unit
            }

            TYPE.TYPE_B -> {
                size / unit / unit / unit
            }

            else -> -1
        }
    }

    /**
     * 转化为 Gb, 格式为 string
     *
     * @param decimalSize   小数位数
     * */
    @SuppressLint("DefaultLocale")
    fun toGbString(size: Long, decimalSize: Int): String {
        return when (type) {
            TYPE.TYPE_GB -> {
                size.toString()
            }

            TYPE.TYPE_MB -> {
                String.format("%.${decimalSize}f", size * 1f / unit)
            }

            TYPE.TYPE_KB -> {
                String.format("%.${decimalSize}f", size * 1f / unit / unit)
            }

            TYPE.TYPE_B -> {
                String.format(
                    "%.${decimalSize}f",
                    size * 1f / unit / unit / unit
                )
            }

            else -> "-1"
        }
    }

    /**
     * 转化为 Mb, 格式为 long
     * */
    fun toMbLong(size: Long): Long {
        return when (type) {
            TYPE.TYPE_GB -> {
                size * unit
            }

            TYPE.TYPE_MB -> {
                size
            }

            TYPE.TYPE_KB -> {
                size / unit
            }

            TYPE.TYPE_B -> {
                size / unit / unit
            }

            else -> -1
        }
    }

    /**
     * 转化为 Mb, 格式为 string
     *
     * @param decimalSize   小数位数
     * */
    fun toMbString(size: Long, decimalSize: Int): String {
        return when (type) {
            TYPE.TYPE_GB -> {
                (size * unit).toString()
            }

            TYPE.TYPE_MB -> {
                size.toString()
            }

            TYPE.TYPE_KB -> {
                String.format("%.${decimalSize}f", size * 1f / unit)
            }

            TYPE.TYPE_B -> {
                String.format("%.${decimalSize}f", size * 1f / unit / unit)
            }

            else -> "-1"
        }
    }

    /**
     * 转化为 Kb, 格式为 long
     * */
    fun toKbLong(size: Long): Long {
        return when (type) {
            TYPE.TYPE_GB -> {
                size * unit * unit
            }

            TYPE.TYPE_MB -> {
                size * unit
            }

            TYPE.TYPE_KB -> {
                size
            }

            TYPE.TYPE_B -> {
                size / unit
            }

            else -> -1
        }
    }

    /**
     * 转化为 Kb, 格式为 string
     *
     * @param decimalSize   小数位数
     * */
    fun toKbString(size: Long, decimalSize: Int): String {
        return when (type) {
            TYPE.TYPE_GB -> {
                (size * unit * unit).toString()
            }

            TYPE.TYPE_MB -> {
                (size * unit).toString()
            }

            TYPE.TYPE_KB -> {
                size.toString()
            }

            TYPE.TYPE_B -> {
                String.format("%.${decimalSize}f", size * 1f / unit)
            }

            else -> "-1"
        }
    }

    /**
     * 转化为 B, 格式为 long
     * */
    fun toBLong(size: Long): Long {
        return when (type) {
            TYPE.TYPE_GB -> {
                size * unit * unit * unit
            }

            TYPE.TYPE_MB -> {
                size * unit * unit
            }

            TYPE.TYPE_KB -> {
                size * unit
            }

            TYPE.TYPE_B -> {
                size
            }

            else -> -1
        }
    }

    /**
     * 转化为 B, 格式为 string
     * */
    fun toBString(size: Long): String {
        return when (type) {
            TYPE.TYPE_GB -> {
                (size * unit * unit * unit).toString()
            }

            TYPE.TYPE_MB -> {
                (size * unit * unit).toString()
            }

            TYPE.TYPE_KB -> {
                (size * unit).toString()
            }

            TYPE.TYPE_B -> {
                size.toString()
            }

            else -> "-1"
        }
    }
}

private object TYPE {
    const val UNIT_1024 = 1024
    const val UNIT_1000 = 1000

    const val TYPE_GB = "gb"
    const val TYPE_MB = "mb"
    const val TYPE_KB = "kb"
    const val TYPE_B = "b"
}