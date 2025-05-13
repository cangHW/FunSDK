package com.proxy.service.core.framework.app.context.cache

/**
 * @author: cangHX
 * @data: 2024/12/27 14:53
 * @desc:
 */
interface ICache<K, V> {

    /**
     * 数据数量
     * */
    fun size(): Int

    /**
     * 异步存数据
     * */
    fun putAsync(key: K, value: V)

    /**
     * 移除数据
     * */
    fun removeAsync(key: K)

    /**
     * 移除数据
     * */
    fun removeAsync(predicate: (Map.Entry<K, V>) -> Boolean)

    /**
     * 获取数据
     * */
    fun get(key: K): V?

    /**
     * 同步遍历
     * */
    fun forEachSync(observer: (Map.Entry<K, V>) -> Unit)

    /**
     * 异步遍历
     * */
    fun forEachAsync(observer: (Map.Entry<K, V>) -> Unit)

    /**
     * 同步获取数据并进行过滤
     * */
    fun filterSync(predicate: (Map.Entry<K, V>) -> Boolean = { true }): Map<K, V>

    /**
     * 异步获取数据并进行过滤
     * */
    fun filterAsync(
        predicate: (Map.Entry<K, V>) -> Boolean = { true },
        observer: (Map.Entry<K, V>) -> Unit
    )
}