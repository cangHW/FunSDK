package com.proxy.service.core.framework.app.worker.work

import android.content.Context
import com.proxy.service.core.framework.app.worker.base.BaseTask

/**
 * @author: cangHX
 * @data: 2024/12/16 11:52
 * @desc:
 */
abstract class AbstractTask : BaseTask() {

    enum class Response {
        /**
         * 成功
         * */
        TYPE_SUCCESS,

        /**
         * 失败
         * */
        TYPE_FAILED;
    }

    /**
     * 执行任务
     *
     * @return 任务执行结果，成功还是失败
     */
    abstract fun doWork(context: Context): Response

}