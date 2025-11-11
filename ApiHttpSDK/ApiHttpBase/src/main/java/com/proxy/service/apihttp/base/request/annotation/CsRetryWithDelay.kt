package com.proxy.service.apihttp.base.request.annotation

import java.util.concurrent.TimeUnit

/**
 * 失败自动重试
 *
 * @author: cangHX
 * @data: 2024/5/21 17:20
 * @desc:
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
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