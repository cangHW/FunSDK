package com.proxy.service.core.framework.collections.base

/**
 * @author: cangHX
 * @data: 2024/12/27 14:53
 * @desc:
 */
interface ISet<V> : IDataChanged<V>, ITransaction {

    /**
     * 数据数量
     * */
    fun size(): Int

    /**
     * 对应数据是否存在
     * */
    fun containsKey(v: V): Boolean

    /**
     * 同步存数据
     * */
    fun putSync(value: V)

    /**
     * 异步存数据
     * */
    fun putAsync(value: V)

    /**
     * 同步移除数据
     * */
    fun removeSync(value: V)

    /**
     * 异步移除数据
     * */
    fun removeAsync(value: V)

    /**
     * 同步移除数据
     * */
    fun removeSync(predicate: (V) -> Boolean)

    /**
     * 异步移除数据
     * */
    fun removeAsync(predicate: (V) -> Boolean)

    /**
     * 同步遍历
     * */
    fun forEachSync(observer: (V) -> Unit)

    /**
     * 异步遍历
     * */
    fun forEachAsync(observer: (V) -> Unit)

    /**
     * 同步获取数据并进行过滤
     * */
    fun filterSync(predicate: (V) -> Boolean = { true }): Set<V>

    /**
     * 异步获取数据并进行过滤
     * */
    fun filterAsync(
        predicate: (V) -> Boolean = { true },
        observer: (V) -> Unit
    )
}