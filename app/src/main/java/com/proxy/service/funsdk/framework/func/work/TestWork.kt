package com.proxy.service.funsdk.framework.func.work

import android.content.Context
import com.proxy.service.core.framework.app.worker.config.ConstraintConfig
import com.proxy.service.core.framework.app.worker.config.InitConfig
import com.proxy.service.core.framework.app.worker.config.RetryConfig
import com.proxy.service.core.framework.app.worker.work.AbstractTask
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/16 17:31
 * @desc:
 */
class TestWork: AbstractTask() {

    override fun doWork(context: Context): Response {
        CsLogger.d("doWork")
        return Response.TYPE_SUCCESS
    }

    override fun getConstraintConfig(): ConstraintConfig? {
        return null
    }

    override fun getInitConfig(): InitConfig {
        return InitConfig.builderLoopTask(15, TimeUnit.MINUTES).build()
    }

    override fun getRetryConfig(): RetryConfig? {
        return null
    }
}