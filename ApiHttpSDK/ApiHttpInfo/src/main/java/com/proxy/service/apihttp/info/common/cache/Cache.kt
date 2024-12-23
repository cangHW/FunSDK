package com.proxy.service.apihttp.info.common.cache

/**
 * @author: cangHX
 * @data: 2024/12/19 18:39
 * @desc:
 */
class Cache<T>(private val maxCount: Int = Int.MAX_VALUE) {

    private val list = ArrayList<T>()

    /**
     * 存储是否已满
     * */
    fun isFull(): Boolean {
        return list.size >= maxCount
    }

    /**
     * 当前缓存数量
     * */
    fun size(): Int {
        return list.size
    }

    /**
     * 添加缓存
     * */
    fun tryAdd(any: T): Boolean {
        if (isFull()) {
            return false
        }
        list.add(any)
        return true
    }

    /**
     * 移除缓存
     * */
    fun remove(any: T): Boolean {
        return list.remove(any)
    }

    /**
     * 获取对应数据
     * */
    fun getOrNull(index: Int): T? {
        return list.getOrNull(index)
    }

    /**
     * 获取全部缓存
     * */
    fun getAllCache(): ArrayList<T> {
        return ArrayList(list)
    }

    /**
     * 清空全部缓存
     * */
    fun clear() {
        list.clear()
    }
}