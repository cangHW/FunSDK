package com.proxy.service.core.framework.app.worker.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.proxy.service.core.framework.app.worker.config.WorkConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.sp.CsSpManager

/**
 * @author: cangHX
 * @data: 2024/12/16 11:17
 * @desc:
 */
class BackgroundWork(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val context = applicationContext
        val className = inputData.getString(WorkConfig.TASK_CLASS_NAME)
        val retryMaxCount = inputData.getInt(WorkConfig.RETRY_COUNT, 0)

        if (className.isNullOrEmpty()) {
            return Result.failure()
        }

        try {
            val task = Class.forName(className).getDeclaredConstructor().newInstance()
            if (task is AbstractTask) {
                val response: AbstractTask.Response = task.doWork(context)
                if (response == AbstractTask.Response.TYPE_SUCCESS) {
                    CsLogger.tag(WorkConfig.TAG).d("run success. task: $className")
                    return success(className)
                }
                val count: Int = CsSpManager.name(WorkConfig.TASK_CACHE_NAME).getInt(className, 0)
                if (count < retryMaxCount) {
                    CsLogger.tag(WorkConfig.TAG)
                        .d("waiting retry, retry count $count and max count $retryMaxCount. task: $className")
                    return retry(className, count)
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(WorkConfig.TAG).e(throwable)
        }
        CsLogger.tag(WorkConfig.TAG).d("run failed. task: $className")
        return failed(className)
    }

    private fun success(className: String): Result {
        CsSpManager.name(WorkConfig.TASK_CACHE_NAME).put(className, 0)
        return Result.success()
    }

    private fun retry(className: String, count: Int): Result {
        CsSpManager.name(WorkConfig.TASK_CACHE_NAME).put(className, count + 1)
        return Result.retry()
    }

    private fun failed(className: String): Result {
        CsSpManager.name(WorkConfig.TASK_CACHE_NAME).put(className, 0)
        return Result.failure()
    }

}