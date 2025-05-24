package com.proxy.service.apihttp.info.common.cache

import java.util.Collections

/**
 * @author: cangHX
 * @data: 2025/3/27 14:12
 * @desc:
 */
class GroupCache<T>(private val comparator: Comparator<T>) {

    private val waitingTasks = ArrayList<T>()
    private val runningTasks = ArrayList<T>()

    /**
     * 等待任务数量
     * */
    fun waitingSize(): Int {
        synchronized(this) {
            return waitingTasks.size
        }
    }

    /**
     * 全部任务数量
     * */
    fun allSize(): Int {
        synchronized(this) {
            return waitingTasks.size + runningTasks.size
        }
    }

    /**
     * 添加缓存
     * */
    fun add(any: T): Boolean {
        synchronized(this) {
            waitingTasks.add(any)
            Collections.sort(waitingTasks, comparator)
        }
        return true
    }

    /**
     * 根据条件移除数据
     * */
    fun removeAll(predicate: (T) -> Boolean) {
        synchronized(this) {
            waitingTasks.removeAll(predicate)
            runningTasks.removeAll(predicate)
        }
    }

    /**
     * 获取第一个等待中数据，如果存在
     * */
    fun getFirstOrNullWaitingValue(): T? {
        synchronized(this) {
            return waitingTasks.firstOrNull()
        }
    }

    /**
     * 设置数据进行执行态
     * */
    fun setWaitingToRunning(any: T): Boolean {
        synchronized(this) {
            if (waitingTasks.remove(any)) {
                return runningTasks.add(any)
            }
        }
        return false
    }

    /**
     * 获取全部数据
     * */
    fun getAllValue(): ArrayList<T> {
        synchronized(this) {
            val allList = ArrayList<T>()
            allList.addAll(waitingTasks)
            allList.addAll(runningTasks)
            return allList
        }
    }
}