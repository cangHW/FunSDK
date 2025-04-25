package com.proxy.service.core.framework.data.time.total.base

import com.proxy.service.core.framework.data.time.enums.TimeFormat
import kotlin.jvm.Throws

/**
 * @author: cangHX
 * @data: 2025/4/14 10:36
 * @desc:
 */
interface ITotalRun {

    /**
     * 获取转换格式后的时间数据
     * */
    fun get(format: TimeFormat): String

    /**
     * 获取转换格式后的时间数据
     *
     * @param pattern 数据格式，可以参考[TimeFormat]
     * */
    @Throws(Throwable::class)
    fun get(pattern: String): String

}