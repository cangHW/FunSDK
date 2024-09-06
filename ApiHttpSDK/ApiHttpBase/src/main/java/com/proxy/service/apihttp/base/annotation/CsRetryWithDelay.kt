package com.proxy.service.apihttp.base.annotation

import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/5/21 17:20
 * @desc: 失败自动重试
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
annotation class CsRetryWithDelay(
    /**
     * 重试次数
     * */
    val retryCount: Int = 3,
    /**
     * 每次重试延迟时间
     * */
    val delay: Long = 2 * 1000,
    /**
     * 每次重试延迟时间格式
     * */
    val unit: TimeUnit = TimeUnit.MILLISECONDS
)