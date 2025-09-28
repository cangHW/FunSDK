package com.proxy.service.core.application.startup

import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.controller.ITaskDisposable
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2025/9/26 11:43
 * @desc:
 */
class InitializerCache<T> {

    companion object {
        const val STATUS_COMPLETE = 0
        const val STATUS_RUNNING = 1
        const val STATUS_WAITING = 2
    }

    private class TaskInfo<T>(private val tClass: Class<*>) {

        companion object {
            private const val DEFAULT_TIME = -1L
        }

        private var waitTime: Long = DEFAULT_TIME
        private var runTime: Long = DEFAULT_TIME
        private var completeTime: Long = DEFAULT_TIME

        private var _status: Int = STATUS_WAITING
        private var _task: T? = null

        fun setStatus(status: Int) {
            _status = status
            when (status) {
                STATUS_WAITING -> {
                    waitTime = System.currentTimeMillis()
                }

                STATUS_RUNNING -> {
                    runTime = System.currentTimeMillis()
                }

                STATUS_COMPLETE -> {
                    completeTime = System.currentTimeMillis()
                }
            }
        }

        fun getStatus(): Int {
            return _status
        }

        fun setTask(task: T?) {
            _task = task
        }

        fun getTask(): T? {
            return _task
        }

        fun getRunTimeLog(): String {
            val builder = StringBuilder()
            builder.append(tClass.simpleName).append("  ")

            builder.append("waiting: ")
            if (runTime != DEFAULT_TIME) {
                if (waitTime != DEFAULT_TIME) {
                    builder.append("${runTime - waitTime}ms")
                } else {
                    builder.append("0ms")
                }
            } else {
                builder.append("timeout")
            }

            builder.append(", ")

            builder.append("running: ")
            if (completeTime != DEFAULT_TIME && runTime != DEFAULT_TIME) {
                builder.append("${completeTime - runTime}ms")
            } else {
                builder.append("timeout")
            }

            return builder.toString()
        }
    }

    private val timeout = AtomicBoolean(false)
    private var disposable: ITaskDisposable? = null

    private val block = LinkedBlockingQueue<Int>()
    private val taskMap = CsExcellentMap<Class<*>, TaskInfo<T>>(true)

    private var totalCount: Int = 0
    private val completeCount = AtomicInteger(0)

    /**
     * 开始计时
     * */
    fun startCounting(count: Int, time: Long, unit: TimeUnit) {
        totalCount = count
        disposable = CsTask.delay(time, unit)
            ?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    // 超时
                    timeout.set(true)
                    block.put(0)
                }
            })?.start()
    }

    /**
     * 设置任务完成
     * */
    fun setTaskComplete(tClass: Class<*>, refresh: Boolean) {
        getTaskInfo(tClass).let {
            it.setStatus(STATUS_COMPLETE)
            it.setTask(null)
        }
        completeCount.getAndIncrement()

        if (isAllTaskComplete()) {
            disposable?.dispose()
        }

        if (refresh) {
            block.put(0)
        }
    }

    /**
     * 设置任务运行
     * */
    fun setTaskRunning(tClass: Class<*>) {
        getTaskInfo(tClass).let {
            it.setStatus(STATUS_RUNNING)
            it.setTask(null)
        }
    }

    /**
     * 设置任务等待
     * */
    fun setTaskWaiting(tClass: Class<*>, value: T) {
        getTaskInfo(tClass).let {
            it.setStatus(STATUS_WAITING)
            it.setTask(value)
        }
    }

    /**
     * 获取任务状态
     * */
    fun getTaskStatus(tClass: Class<*>): Int {
        return getTaskInfo(tClass).getStatus()
    }

    /**
     * 对应任务是否完成
     * */
    fun isTaskComplete(tClass: Class<*>): Boolean {
        return getTaskInfo(tClass).getStatus() == STATUS_COMPLETE
    }

    /**
     * 对应任务是否正在运行
     * */
    fun isTaskRunning(tClass: Class<*>): Boolean {
        return getTaskInfo(tClass).getStatus() == STATUS_RUNNING
    }

    /**
     * 对应任务是否运行中或已完成
     * */
    fun isTaskRunningOrComplete(tClass: Class<*>): Boolean {
        val value = getTaskInfo(tClass).getStatus()
        return value == STATUS_RUNNING || value == STATUS_COMPLETE
    }

    /**
     * 全部任务是否完成
     * */
    fun isAllTaskComplete(): Boolean {
        return completeCount.get() == totalCount
    }

    /**
     * 是否超时
     * */
    fun isTimeout(): Boolean {
        return timeout.get()
    }

    /**
     * 等待任意任务完成
     * */
    fun awaitAnyTaskComplete() {
        block.take()
    }

    /**
     * 遍历等待中的任务
     * */
    fun forEachWaitingTask(observer: (T) -> Unit) {
        taskMap.forEachSync { _, taskInfo ->
            if (taskInfo.getStatus() == STATUS_WAITING) {
                taskInfo.getTask()?.let {
                    observer(it)
                }
            }
        }
    }

    /**
     * 遍历任务日志
     * */
    fun forEachTaskLog(observer: (String) -> Unit) {
        taskMap.forEachSync { _, taskInfo ->
            observer(taskInfo.getRunTimeLog())
        }
    }

    private fun getTaskInfo(tClass: Class<*>): TaskInfo<T> {
        var value = taskMap.get(tClass)
        if (value == null) {
            value = TaskInfo(tClass)
            taskMap.putSync(tClass, value)
        }
        return value
    }
}