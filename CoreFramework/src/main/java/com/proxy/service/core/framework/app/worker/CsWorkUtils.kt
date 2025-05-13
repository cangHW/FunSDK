package com.proxy.service.core.framework.app.worker

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.WorkManager
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.worker.config.ConstraintConfig
import com.proxy.service.core.framework.app.worker.config.InitConfig
import com.proxy.service.core.framework.app.worker.config.RetryConfig
import com.proxy.service.core.framework.app.worker.config.WorkConfig
import com.proxy.service.core.framework.app.worker.work.AbstractTask
import java.util.concurrent.TimeUnit

/**
 * 定时任务以及轮训任务工具, 可提供在进程未创建时的自动唤醒能力
 *
 * @author: cangHX
 * @data: 2024/12/16 15:43
 * @desc:
 */
object CsWorkUtils {

    /**
     * 开始任务
     */
    fun <T : AbstractTask> start(tClass: Class<T>) {
        val task = tClass.getDeclaredConstructor().newInstance()

        val uniqueWorkName = tClass.name
        val initConfig = task.getInitConfig() ?: InitConfig.builderOnceTask().build()
        val constraintConfig = task.getConstraintConfig() ?: ConstraintConfig.builder().build()
        val retryConfig = task.getRetryConfig() ?: RetryConfig.builder().build()

        val dataBuilder = Data.Builder()
        dataBuilder.putString(WorkConfig.TASK_CLASS_NAME, uniqueWorkName)
        dataBuilder.putInt(WorkConfig.RETRY_COUNT, retryConfig.getRetryCount())

        val request = initConfig.getWorkType()
            .createWorkRequest(initConfig)
            .setInitialDelay(initConfig.getDelayDuration(), TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(constraintConfig.getRequiredNetworkType().type)
                    .setRequiresCharging(constraintConfig.getRequiresCharging())
                    .setRequiresDeviceIdle(constraintConfig.getRequiresDeviceIdle())
                    .setRequiresBatteryNotLow(constraintConfig.getRequiresBatteryNotLow())
                    .setRequiresStorageNotLow(constraintConfig.getRequiresStorageNotLow())
                    .build()
            )
            .setBackoffCriteria(
                retryConfig.getRetryPolicy().policy,
                retryConfig.getRetryDelay(),
                TimeUnit.MILLISECONDS
            )
            .setInputData(dataBuilder.build())
            .build()

        initConfig.getWorkType().startTask(uniqueWorkName, initConfig.getWorkPolicy(), request)
    }

    /**
     * 结束任务
     */
    fun <T : AbstractTask> cancel(tClass: Class<T>) {
        val uniqueWorkName = tClass.name
        WorkManager.getInstance(CsContextManager.getApplication()).cancelUniqueWork(uniqueWorkName)
    }

}