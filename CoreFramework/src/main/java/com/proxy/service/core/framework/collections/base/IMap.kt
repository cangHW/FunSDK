package com.proxy.service.core.framework.collections.base

/**
 * @author: cangHX
 * @data: 2024/12/27 14:53
 * @desc:
 */
interface IMap<K, V> {

    /**
     * 数据数量
     * */
    fun size(): Int

    /**
     * 同步存数据
     * */
    fun putSync(key: K, value: V)

    /**
     * 异步存数据
     * */
    fun putAsync(key: K, value: V)

    /**
     * 同步移除数据
     * */
    fun removeSync(key: K)

    /**
     * 异步移除数据
     * */
    fun removeAsync(key: K)

    /**
     * 同步移除数据
     * */
    fun removeSync(predicate: (K, V) -> Boolean)

    /**
     * 异步移除数据
     * */
    fun removeAsync(predicate: (K, V) -> Boolean)

    /**
     * 获取数据
     * */
    fun get(key: K): V?

    /**
     * 同步遍历
     * */
    fun forEachSync(observer: (K, V) -> Unit)

    /**
     * 异步遍历
     * */
    fun forEachAsync(observer: (K, V) -> Unit)

    /**
     * 同步获取数据并进行过滤
     * */
    fun filterSync(predicate: (K, V) -> Boolean = { _, _ -> true }): Map<K, V>

    /**
     * 异步获取数据并进行过滤
     * */
    fun filterAsync(
        predicate: (K, V) -> Boolean = { _, _ -> true },
        observer: (K, V) -> Unit
    )
}