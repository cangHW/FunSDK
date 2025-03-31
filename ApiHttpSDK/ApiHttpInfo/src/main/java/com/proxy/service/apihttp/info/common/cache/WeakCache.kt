package com.proxy.service.apihttp.info.common.cache

import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2025/3/27 10:15
 * @desc:
 */
class WeakCache<T> {

    companion object {
        private val DEFAULT = Any()
    }

    private val weakHashMap = WeakHashMap<T, Any>()

    /**
     * 当前缓存数量
     * */
    fun size(): Int {
        synchronized(this) {
            return weakHashMap.size
        }
    }

    /**
     * 添加缓存
     * */
    fun add(any: T) {
        synchronized(this) {
            weakHashMap[any] = DEFAULT
        }
    }

    /**
     * 移除缓存
     * */
    fun remove(any: T?) {
        synchronized(this) {
            weakHashMap.remove(any)
        }
    }

    /**
     * 条件过滤
     * */
    fun filter(predicate: (T) -> Boolean): ArrayList<T> {
        val list = ArrayList<T>()
        val keys = synchronized(this) {
            weakHashMap.filter {
                predicate(it.key)
            }.keys
        }
        list.addAll(keys)
        return list
    }

    /**
     * 获取全部缓存
     * */
    fun getAllCache(): ArrayList<T> {
        synchronized(this) {
            return ArrayList(weakHashMap.keys)
        }
    }
}