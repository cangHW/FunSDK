package com.proxy.service.core.framework.collections

import com.proxy.service.core.framework.collections.base.ISet

/**
 * 支持异步操作 set
 *
 * @author: cangHX
 * @data: 2025/5/23 18:40
 * @desc:
 */
class CsExcellentSet<K> : ISet<K> {

    companion object {
        private val any = Any()
    }

    private val map = CsExcellentMap<K, Any>()

    override fun size(): Int {
        return map.size()
    }

    override fun putSync(key: K) {
        map.putSync(key, any)
    }

    override fun removeSync(predicate: (K) -> Boolean) {
        map.removeSync { key, _ ->
            predicate(key)
        }
    }

    override fun removeSync(key: K) {
        map.removeSync(key)
    }

    override fun filterAsync(predicate: (K) -> Boolean, observer: (K) -> Unit) {
        map.filterAsync(
            { key, _ ->
                predicate(key)
            },
            { key, _ ->
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

    override fun removeAsync(key: K) {
        map.removeAsync(key)
    }

    override fun putAsync(key: K) {
        map.putAsync(key, any)
    }

}