package com.proxy.service.core.framework.collections

import com.proxy.service.core.framework.collections.base.IMap
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @author: cangHX
 * @data: 2024/12/27 14:45
 * @desc:
 */
open class CsExcellentMap<K, V> : IMap<K, V> {

    private val lock = ReentrantReadWriteLock()
    private val read = lock.readLock()
    private val write = lock.writeLock()

    private val map = HashMap<K, V>()

    override fun size(): Int {
        return map.size
    }

    override fun putSync(key: K, value: V) {
        write.lock()
        try {
            if (!map.containsKey(key)) {
                map.put(key, value)
            }
        } finally {
            write.unlock()
        }
    }

    override fun removeSync(predicate: (K, V) -> Boolean) {
        val temp: Map<K, V>
        write.lock()
        try {
            temp = map.filter {
                predicate(it.key, it.value)
            }
        } finally {
            write.unlock()
        }

        temp.forEach {
            removeSync(it.key)
        }
    }

    override fun removeSync(key: K) {
        write.lock()
        try {
            map.remove(key)
        } finally {
            write.unlock()
        }
    }

    override fun putAsync(key: K, value: V) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                putSync(key, value)
                return ""
            }
        })?.start()
    }

    override fun removeAsync(key: K) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                removeSync(key)
                return ""
            }
        })?.start()
    }

    override fun removeAsync(predicate: (K, V) -> Boolean) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                removeSync(predicate)
                return ""
            }
        })?.start()
    }

    override fun get(key: K): V? {
        read.lock()
        try {
            return map.get(key)
        } finally {
            read.unlock()
        }
    }

    override fun forEachAsync(observer: (K, V) -> Unit) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                forEachSync(observer)
                return ""
            }
        })?.start()
    }

    override fun forEachSync(observer: (K, V) -> Unit) {
        HashMap(map).forEach {
            observer(it.key, it.value)
        }
    }

    override fun filterSync(predicate: (K, V) -> Boolean): Map<K, V> {
        read.lock()
        try {
            return map.filter {
                predicate(it.key, it.value)
            }
        } finally {
            read.unlock()
        }
    }

    override fun filterAsync(
        predicate: (K, V) -> Boolean,
        observer: (K, V) -> Unit
    ) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                filterSync(predicate).forEach {
                    observer(it.key, it.value)
                }
                return ""
            }
        })?.start()
    }

}