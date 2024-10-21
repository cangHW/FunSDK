package com.proxy.service.apihttp.base.annotation

import java.util.concurrent.TimeUnit

/**
 * 精确的超时控制，包含 Dns 解析时间
 *
 * @author: cangHX
 * @data: 2024/5/21 17:11
 * @desc:
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class CsExactTimeOut(
    /**
     * 超时时间
     * */
    val timeout: Long,
    /**
     * 超时时间格式
     * */
    val unit: TimeUnit
)
