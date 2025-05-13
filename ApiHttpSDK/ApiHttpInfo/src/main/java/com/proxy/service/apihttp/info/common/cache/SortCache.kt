package com.proxy.service.apihttp.info.common.cache

import java.util.Collections

/**
 * @author: cangHX
 * @data: 2025/3/27 10:51
 * @desc:
 */
class SortCache<K, V>(private val comparator: Comparator<V>) {

    private val map = HashMap<K, V>()
    private val list = ArrayList<V>()

    /**
     * 是否存在
     * */
    fun isHas(key: K): Boolean {
        synchronized(this) {
            return map.containsKey(key)
        }
    }

    /**
     * 添加缓存
     * */
    fun tryAdd(key: K, value: V): Boolean {
        synchronized(this) {
            if (isHas(key)) {
                return false
            }
            map[key] = value
            list.add(value)
            Collections.sort(list, comparator)
        }
        return true
    }

    /**
     * 移除缓存
     * */
    fun remove(key: K) {
        synchronized(this) {
            map.remove(key)?.let {
                list.remove(it)
            }
        }
    }

    /**
     * 获取对应数据
     * */
    fun getOrNull(key: K): V? {
        synchronized(this) {
            return map[key]
        }
    }

    /**
     * 获取全部缓存
     * */
    fun getAllValues(): ArrayList<V> {
        synchronized(this) {
            return ArrayList(list)
        }
    }
}