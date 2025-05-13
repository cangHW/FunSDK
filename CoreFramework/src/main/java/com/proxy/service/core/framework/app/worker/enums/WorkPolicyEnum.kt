package com.proxy.service.core.framework.app.worker.enums

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy

/**
 * @author: cangHX
 * @data: 2024/1/4 10:25
 * @desc:
 */
enum class WorkPolicyEnum(
    val policy: ExistingWorkPolicy,
    val periodicPolicy: ExistingPeriodicWorkPolicy
) {

    /**
     * 替换
     * */
    REPLACE(ExistingWorkPolicy.REPLACE, ExistingPeriodicWorkPolicy.REPLACE),

    /**
     * 保持
     * */
    KEEP(ExistingWorkPolicy.KEEP, ExistingPeriodicWorkPolicy.KEEP);

}