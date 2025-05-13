package com.proxy.service.threadpool.info.handler.manager

import com.proxy.service.threadpool.base.handler.controller.ITaskDisposable

/**
 * @author: cangHX
 * @data: 2025/3/18 10:49
 * @desc:
 */
class TaskInfo private constructor(
    val tag: String = "",
    val runnable: Runnable
) {

    companion object {

        /**
         * @param runnable 任务
         * */
        fun create(runnable: Runnable): TaskInfo {
            return TaskInfo("", runnable)
        }

        /**
         * @param tag       任务标签
         * @param runnable  任务
         * */
        fun create(tag: String, runnable: Runnable): TaskInfo {
            return TaskInfo(tag, runnable)
        }
    }
}