package com.proxy.service.core.framework.convert

import android.annotation.SuppressLint

/**
 * 存储数据转换器
 *
 * @author: cangHX
 * @data: 2024/7/26 11:38
 * @desc:
 */
enum class CsStorageUnit(private val type: String) {

    GB(TYPE.TYPE_GB),
    MB(TYPE.TYPE_MB),
    KB(TYPE.TYPE_KB),
    B(TYPE.TYPE_B);

    /**
     * 转化为 Gb, 格式为 long
     * */
    fun toGbLong(size: Long): Long {
        return when (type) {
            TYPE.TYPE_GB -> {
                size
            }

            TYPE.TYPE_MB -> {
                size / TYPE.OFFSET
            }

            TYPE.TYPE_KB -> {
                size / TYPE.OFFSET / TYPE.OFFSET
            }

            TYPE.TYPE_B -> {
                size / TYPE.OFFSET / TYPE.OFFSET / TYPE.OFFSET
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
                String.format("%.${decimalSize}f", size * 1f / TYPE.OFFSET)
            }

            TYPE.TYPE_KB -> {
                String.format("%.${decimalSize}f", size * 1f / TYPE.OFFSET / TYPE.OFFSET)
            }

            TYPE.TYPE_B -> {
                String.format(
                    "%.${decimalSize}f",
                    size * 1f / TYPE.OFFSET / TYPE.OFFSET / TYPE.OFFSET
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
                size * TYPE.OFFSET
            }

            TYPE.TYPE_MB -> {
                size
            }

            TYPE.TYPE_KB -> {
                size / TYPE.OFFSET
            }

            TYPE.TYPE_B -> {
                size / TYPE.OFFSET / TYPE.OFFSET
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
                (size * TYPE.OFFSET).toString()
            }

            TYPE.TYPE_MB -> {
                size.toString()
            }

            TYPE.TYPE_KB -> {
                String.format("%.${decimalSize}f", size * 1f / TYPE.OFFSET)
            }

            TYPE.TYPE_B -> {
                String.format("%.${decimalSize}f", size * 1f / TYPE.OFFSET / TYPE.OFFSET)
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
                size * TYPE.OFFSET * TYPE.OFFSET
            }

            TYPE.TYPE_MB -> {
                size * TYPE.OFFSET
            }

            TYPE.TYPE_KB -> {
                size
            }

            TYPE.TYPE_B -> {
                size / TYPE.OFFSET
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
                (size * TYPE.OFFSET * TYPE.OFFSET).toString()
            }

            TYPE.TYPE_MB -> {
                (size * TYPE.OFFSET).toString()
            }

            TYPE.TYPE_KB -> {
                size.toString()
            }

            TYPE.TYPE_B -> {
                String.format("%.${decimalSize}f", size * 1f / TYPE.OFFSET)
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
                size * TYPE.OFFSET * TYPE.OFFSET * TYPE.OFFSET
            }

            TYPE.TYPE_MB -> {
                size * TYPE.OFFSET * TYPE.OFFSET
            }

            TYPE.TYPE_KB -> {
                size * TYPE.OFFSET
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
                (size * TYPE.OFFSET * TYPE.OFFSET * TYPE.OFFSET).toString()
            }

            TYPE.TYPE_MB -> {
                (size * TYPE.OFFSET * TYPE.OFFSET).toString()
            }

            TYPE.TYPE_KB -> {
                (size * TYPE.OFFSET).toString()
            }

            TYPE.TYPE_B -> {
                size.toString()
            }

            else -> "-1"
        }
    }
}

private object TYPE {
    const val OFFSET = 1024

    const val TYPE_GB = "gb"
    const val TYPE_MB = "mb"
    const val TYPE_KB = "kb"
    const val TYPE_B = "b"
}