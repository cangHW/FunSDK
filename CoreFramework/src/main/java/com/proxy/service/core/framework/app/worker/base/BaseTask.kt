package com.proxy.service.core.framework.app.worker.base

import com.proxy.service.core.framework.app.worker.config.ConstraintConfig
import com.proxy.service.core.framework.app.worker.config.InitConfig
import com.proxy.service.core.framework.app.worker.config.RetryConfig

/**
 * @author: cangHX
 * @data: 2024/12/16 11:14
 * @desc:
 */
abstract class BaseTask {

    /**
     * 初始执行配置
     */
    abstract fun getInitConfig(): InitConfig?

    /**
     * 执行条件配置
     */
    abstract fun getConstraintConfig(): ConstraintConfig?

    /**
     * 执行失败重试配置
     */
    abstract fun getRetryConfig(): RetryConfig?

}