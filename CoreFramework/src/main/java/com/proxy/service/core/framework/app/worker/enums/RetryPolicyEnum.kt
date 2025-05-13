package com.proxy.service.core.framework.app.worker.enums

import androidx.work.BackoffPolicy

/**
 * @author: cangHX
 * @data: 2024/1/3 17:26
 * @desc:
 */
enum class RetryPolicyEnum(val policy: BackoffPolicy) {

    /**
     * 指数策略。
     * 每次重试的延迟时间将增加两倍。
     * 例如，如果初始延迟时间是 1 秒，那么第一次重试的延迟时间将是 2 秒，第二次重试的延迟时间将是 4 秒，以此类推。
     * */
    EXPONENTIAL(BackoffPolicy.EXPONENTIAL),

    /**
     * 线性策略。
     * 每次重试的延迟时间将增加一个固定的量。
     * 例如，如果初始延迟时间是 1 秒，那么第一次重试的延迟时间将是 2 秒，第二次重试的延迟时间将是 3 秒，以此类推。
     * */
    LINEAR(BackoffPolicy.LINEAR);

}