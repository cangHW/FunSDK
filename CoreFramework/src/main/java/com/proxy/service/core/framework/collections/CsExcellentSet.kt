package com.proxy.service.core.framework.collections

import com.proxy.service.core.framework.collections.base.ISet
import com.proxy.service.core.framework.collections.callback.OnDataChangedCallback
import com.proxy.service.core.framework.collections.type.Type

/**
 * 线程安全、支持异步操作 set
 *
 * @author: cangHX
 * @data: 2025/5/23 18:40
 * @desc:
 */
class CsExcellentSet<K>(
    /**
     * 模式
     * */
    type: Type = Type.NORMAL
) : ISet<K> {

    companion object {
        private val any = Any()
    }

    private val dataChangedCallbacks = CsExcellentList<OnDataChangedCallback<K>>()
    private val dataChangedCallbackImpl = DataChangedCallbackImpl(dataChangedCallbacks)
    private val map = CsExcellentMap<K, Any>(type)

    init {
        map.addDataChangedCallback(dataChangedCallbackImpl)
    }

    override fun removeDataChangedCallback(callback: OnDataChangedCallback<K>) {
        dataChangedCallbacks.removeSync(callback)
    }

    override fun addDataChangedCallback(callback: OnDataChangedCallback<K>) {
        dataChangedCallbacks.putSync(callback)
    }

    override fun containsKey(v: K): Boolean {
        return map.containsKey(v)
    }

    override fun size(): Int {
        return map.size()
    }

    override fun getAll(): HashSet<K> {
        return map.keySet()
    }

    override fun runInTransaction(runnable: Runnable) {
        map.runInTransaction(runnable)
    }

    override fun putSync(v: K) {
        map.runInTransaction {
            if (map.containsKey(v)) {
                return@runInTransaction
            }
            map.putSync(v, any)
        }
    }

    override fun removeSync(predicate: (K) -> Boolean) {
        map.removeSync { key, _ ->
            predicate(key)
        }
    }

    override fun removeSync(v: K) {
        map.removeSync(v)
    }

    override fun filterAsync(predicate: (K) -> Boolean, observer: (K) -> Unit) {
        map.filterAsync(
            predicate = { key, _ ->
                predicate(key)
            },
            observer = { key, _ ->
                observer(key)
            }
        )
    }

    override fun filterSync(predicate: (K) -> Boolean): Set<K> {
        return map.filterSync { key, _ ->
            predicate(key)
        }.keys
    }

    override fun forEachAsync(observer: (K) -> Unit) {
        map.forEachAsync { key, _ ->
            observer(key)
        }
    }

    override fun forEachSync(observer: (K) -> Unit) {
        map.forEachSync { key, _ ->
            observer(key)
        }
    }

    override fun removeAsync(predicate: (K) -> Boolean) {
        map.removeAsync { key, _ ->
            predicate(key)
        }
    }

    override fun removeAsync(v: K) {
        map.removeAsync(v)
    }

    override fun putAsync(v: K) {
        map.putAsync(v, any)
    }

    private class DataChangedCallbackImpl<K>(
        private val list: CsExcellentList<OnDataChangedCallback<K>>
    ) : OnDataChangedCallback<Map.Entry<K, Any>>() {

        override fun onDataAdd(t: Map.Entry<K, Any>) {
            list.forEachSync {
                it.onDataAdd(t.key)
            }
        }

        override fun onDataRemoved(t: Map.Entry<K, Any>) {
            list.forEachSync {
                it.onDataRemoved(t.key)
            }
        }

    }

}