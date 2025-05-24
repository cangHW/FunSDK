package com.proxy.service.core.framework.collections.base

/**
 * @author: cangHX
 * @data: 2024/12/27 14:53
 * @desc:
 */
interface ISet<K> {

    /**
     * 数据数量
     * */
    fun size(): Int

    /**
     * 同步存数据
     * */
    fun putSync(key: K)

    /**
     * 异步存数据
     * */
    fun putAsync(key: K)

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
    fun removeSync(predicate: (K) -> Boolean)

    /**
     * 异步移除数据
     * */
    fun removeAsync(predicate: (K) -> Boolean)

    /**
     * 同步遍历
     * */
    fun forEachSync(observer: (K) -> Unit)

    /**
     * 异步遍历
     * */
    fun forEachAsync(observer: (K) -> Unit)

    /**
     * 同步获取数据并进行过滤
     * */
    fun filterSync(predicate: (K) -> Boolean = { true }): Set<K>

    /**
     * 异步获取数据并进行过滤
     * */
    fun filterAsync(
        predicate: (K) -> Boolean = { true },
        observer: (K) -> Unit
    )
}