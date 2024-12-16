package com.proxy.service.core.framework.app.worker.enums

import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.worker.config.InitConfig
import com.proxy.service.core.framework.app.worker.config.WorkConfig
import com.proxy.service.core.framework.app.worker.work.BackgroundWork
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/1/4 10:19
 * @desc:
 */
enum class WorkTypeEnum {

    /**
     * 单次任务
     */
    ONCE {
        override fun createWorkRequest(config: InitConfig): WorkRequest.Builder<*, *> {
            return OneTimeWorkRequest.Builder(BackgroundWork::class.java)
        }

        override fun startTask(
            uniqueWorkName: String,
            policy: WorkPolicyEnum,
            request: WorkRequest
        ) {
            if (request is OneTimeWorkRequest) {
                val operation =
                    WorkManager.getInstance(CsContextManager.getApplication()).enqueueUniqueWork(
                        uniqueWorkName,
                        policy.policy,
                        request
                    )
                operation.state.observeForever(object : Observer<Operation.State> {
                    override fun onChanged(value: Operation.State) {
                        CsLogger.tag(WorkConfig.TAG).i("uniqueWorkName: $uniqueWorkName startTask: $value")

                        if (value is Operation.State.SUCCESS || value is Operation.State.FAILURE) {
                            operation.state.removeObserver(this)
                        }
                    }
                })
            }
        }
    },

    /**
     * 循环任务
     */
    LOOP {
        override fun createWorkRequest(config: InitConfig): WorkRequest.Builder<*, *> {
            return PeriodicWorkRequest.Builder(
                BackgroundWork::class.java,
                config.getLoopDuration(),
                TimeUnit.MILLISECONDS,
                config.getFlexDuration(),
                TimeUnit.MILLISECONDS
            )
        }

        override fun startTask(
            uniqueWorkName: String,
            policy: WorkPolicyEnum,
            request: WorkRequest
        ) {
            if (request is PeriodicWorkRequest) {
                val operation =
                    WorkManager.getInstance(CsContextManager.getApplication())
                        .enqueueUniquePeriodicWork(
                            uniqueWorkName,
                            policy.periodicPolicy,
                            request
                        )
                operation.state.observeForever(object : Observer<Operation.State> {
                    override fun onChanged(value: Operation.State) {
                        CsLogger.tag(WorkConfig.TAG).i("uniqueWorkName: $uniqueWorkName startTask: $value")

                        if (value is Operation.State.SUCCESS || value is Operation.State.FAILURE) {
                            operation.state.removeObserver(this)
                        }
                    }
                })
            }
        }
    };

    /**
     * 创建任务请求器
     * */
    abstract fun createWorkRequest(config: InitConfig): WorkRequest.Builder<*, *>

    /**
     * 开始任务
     * */
    abstract fun startTask(uniqueWorkName: String, policy: WorkPolicyEnum, request: WorkRequest)

}