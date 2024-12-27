package com.proxy.service.core.framework.app.context.cache

import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * @author: cangHX
 * @data: 2024/12/27 14:45
 * @desc:
 */
class SynchronizedCache<K, V> : ICache<K, V> {

    private val lock = ReentrantReadWriteLock()
    private val read = lock.readLock()
    private val write = lock.writeLock()

    private val map = HashMap<K, V>()

    override fun size(): Int {
        return map.size
    }

    override fun putAsync(key: K, value: V) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                write.lock()
                try {
                    if (!map.containsKey(key)) {
                        map.put(key, value)
                    }
                } finally {
                    write.unlock()
                }
                return ""
            }
        })?.start()
    }

    override fun removeAsync(key: K) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                write.lock()
                try {
                    map.remove(key)
                } finally {
                    write.unlock()
                }
                return ""
            }
        })?.start()
    }

    override fun removeAsync(predicate: (Map.Entry<K, V>) -> Boolean) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val temp: Map<K, V>
                write.lock()
                try {
                    temp = map.filter(predicate)
                } finally {
                    write.unlock()
                }

                temp.forEach {
                    removeAsync(it.key)
                }

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

    override fun forEachAsync(observer: (Map.Entry<K, V>) -> Unit) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                forEachSync(observer)
                return ""
            }
        })?.start()
    }

    override fun forEachSync(observer: (Map.Entry<K, V>) -> Unit) {
        HashMap(map).forEach {
            observer(it)
        }
    }

    override fun filterSync(predicate: (Map.Entry<K, V>) -> Boolean): Map<K, V> {
        read.lock()
        try {
            return map.filter(predicate)
        } finally {
            read.unlock()
        }
    }

    override fun filterAsync(
        predicate: (Map.Entry<K, V>) -> Boolean,
        observer: (Map.Entry<K, V>) -> Unit
    ) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val temp: Map<K, V>
                read.lock()
                try {
                    temp = map.filter(predicate)
                } finally {
                    read.unlock()
                }

                temp.forEach {
                    observer(it)
                }
                return ""
            }
        })?.start()
    }

}