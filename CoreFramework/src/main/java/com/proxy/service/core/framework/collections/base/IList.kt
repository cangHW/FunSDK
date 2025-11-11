package com.proxy.service.core.framework.collections.base

/**
 * @author: cangHX
 * @data: 2025/9/17 15:25
 * @desc:
 */
interface IList<V> : IDataChanged<V>, ITransaction {

    /**
     * 数据数量
     * */
    fun size(): Int

    /**
     * 清空数据
     * */
    fun clear()

    /**
     * 对应数据是否存在
     * */
    fun containsKey(v: V): Boolean

    /**
     * 获取数据
     * */
    fun get(position: Int): V?

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
     * 同步移除全部符合条件的数据
     * */
    fun removeSync(predicate: (V) -> Boolean)

    /**
     * 异步移除全部符合条件的数据
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
    fun filterSync(predicate: (V) -> Boolean): List<V>

    /**
     * 异步获取数据并进行过滤
     * */
    fun filterAsync(predicate: (V) -> Boolean, observer: (V) -> Unit)

}