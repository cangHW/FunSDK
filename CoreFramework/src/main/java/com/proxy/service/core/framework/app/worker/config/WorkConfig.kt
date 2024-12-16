package com.proxy.service.core.framework.app.worker.config

import com.proxy.service.core.constants.Constants

/**
 * @author: cangHX
 * @data: 2024/1/3 18:26
 * @desc:
 */
object WorkConfig {

    /**
     * task 相关日志 tag
     */
    const val TAG: String = "${Constants.TAG}Worker"

    /**
     * task 临时数据缓存文件名称
     */
    const val TASK_CACHE_NAME: String = "work_task_cache"

    /**
     * task 全名
     */
    const val TASK_CLASS_NAME: String = "TASK_CLASS_NAME"

    /**
     * 重试次数
     */
    const val RETRY_COUNT: String = "RETRY_COUNT"
}
