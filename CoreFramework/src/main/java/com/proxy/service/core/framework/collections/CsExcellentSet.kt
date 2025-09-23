package com.proxy.service.core.framework.collections

import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.collections.callback.OnDataChangedCallback

/**
 * 线程安全、支持异步操作 set
 *
 * @author: cangHX
 * @data: 2025/5/23 18:40
 * @desc:
 */
class CsExcellentSet<V> : ISet<V> {

    companion object {
        private val any = Any()
    }

    private val dataChangedCallbacks = CsExcellentList<OnDataChangedCallback<V>>()
    private val dataChangedCallbackImpl = DataChangedCallbackImpl(dataChangedCallbacks)
    private val map = CsExcellentMap<V, Any>()

    init {
        map.addDataChangedCallback(dataChangedCallbackImpl)
    }

    override fun removeDataChangedCallback(callback: OnDataChangedCallback<V>) {
        dataChangedCallbacks.removeSync(callback)
    }

    override fun addDataChangedCallback(callback: OnDataChangedCallback<V>) {
        dataChangedCallbacks.putSync(callback)
    }

    override fun containsKey(v: V): Boolean {
        return map.containsKey(v)
    }

    override fun size(): Int {
        return map.size()
    }

    override fun runInTransaction(runnable: Runnable) {
        map.runInTransaction(runnable)
    }

    override fun putSync(value: V) {
        map.runInTransaction {
            if (map.containsKey(value)) {
                return@runInTransaction
            }
            map.putSync(value, any)
        }
    }

    override fun removeSync(predicate: (V) -> Boolean) {
        map.removeSync { key, _ ->
            predicate(key)
        }
    }

    override fun removeSync(value: V) {
        map.removeSync(value)
    }

    override fun filterAsync(predicate: (V) -> Boolean, observer: (V) -> Unit) {
        map.filterAsync(
            predicate = { key, _ ->
                predicate(key)
            },
            observer = { key, _ ->
                observer(key)
            }
        )
    }

    override fun filterSync(predicate: (V) -> Boolean): Set<V> {
        return map.filterSync { key, _ ->
            predicate(key)
        }.keys
    }

    override fun forEachAsync(observer: (V) -> Unit) {
        map.forEachAsync { key, _ ->
            observer(key)
        }
    }

    override fun forEachSync(observer: (V) -> Unit) {
        map.forEachSync { key, _ ->
            observer(key)
        }
    }

    override fun removeAsync(predicate: (V) -> Boolean) {
        map.removeAsync { key, _ ->
            predicate(key)
        }
    }

    override fun removeAsync(value: V) {
        map.removeAsync(value)
    }

    override fun putAsync(value: V) {
        map.putAsync(value, any)
    }

    private class DataChangedCallbackImpl<V>(
        private val list: CsExcellentList<OnDataChangedCallback<V>>
    ) : OnDataChangedCallback<Map.Entry<V, Any>>() {

        override fun onDataAdd(t: Map.Entry<V, Any>) {
            list.forEachSync {
                it.onDataAdd(t.key)
            }
        }

        override fun onDataRemoved(t: Map.Entry<V, Any>) {
            list.forEachSync {
                it.onDataRemoved(t.key)
            }
        }

    }

}