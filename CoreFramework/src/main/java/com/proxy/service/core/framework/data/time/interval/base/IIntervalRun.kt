package com.proxy.service.core.framework.data.time.interval.base

import com.proxy.service.core.framework.data.time.enums.TimeIntervalFormat
import kotlin.jvm.Throws

/**
 * @author: cangHX
 * @data: 2025/4/14 10:36
 * @desc:
 */
interface IIntervalRun {

    /**
     * 获取转换格式后的时间数据
     * */
    fun get(format: TimeIntervalFormat): String

    /**
     * 获取转换格式后的时间数据
     *
     * @param pattern 数据格式，可以参考[TimeIntervalFormat]
     * */
    @Throws(Throwable::class)
    fun get(pattern: String): String

}