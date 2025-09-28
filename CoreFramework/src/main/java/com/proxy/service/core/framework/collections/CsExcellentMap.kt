package com.proxy.service.core.framework.collections

import com.proxy.service.core.framework.collections.base.IMap
import com.proxy.service.core.framework.collections.callback.OnDataChangedCallback
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * 线程安全、支持异步操作以及有序、无序的 map
 *
 * @author: cangHX
 * @data: 2024/12/27 14:45
 * @desc:
 */
open class CsExcellentMap<K, V>(
    /**
     * 是否有序
     * */
    isOrder: Boolean = false
) : IMap<K, V> {

    private val lock = ReentrantReadWriteLock()
    private val read = lock.readLock()
    private val write = lock.writeLock()

    private val dataChangedCallbacks = CsExcellentList<OnDataChangedCallback<Map.Entry<K, V>>>()
    private val map = if (isOrder) {
        LinkedHashMap<K, V>()
    } else {
        HashMap<K, V>()
    }

    override fun size(): Int {
        return map.size
    }

    override fun containsKey(k: K): Boolean {
        read.lock()
        try {
            return map.containsKey(k)
        } finally {
            read.unlock()
        }
    }

    override fun runInTransaction(runnable: Runnable) {
        write.lock()
        try {
            runnable.run()
        } finally {
            write.unlock()
        }
    }

    override fun removeDataChangedCallback(callback: OnDataChangedCallback<Map.Entry<K, V>>) {
        dataChangedCallbacks.removeSync(callback)
    }

    override fun addDataChangedCallback(callback: OnDataChangedCallback<Map.Entry<K, V>>) {
        dataChangedCallbacks.putSync(callback)
    }

    override fun putSync(key: K, value: V): Boolean {
        write.lock()
        try {
            map.put(key, value)
        } finally {
            write.unlock()
        }
        sendDataAdd(key, value)
        return true
    }

    override fun removeSync(predicate: (K, V) -> Boolean) {
        filterSync(predicate).forEach {
            removeSync(it.key)
        }
    }

    override fun removeSync(key: K): V? {
        val value: V?
        write.lock()
        try {
            value = map.remove(key)
        } finally {
            write.unlock()
        }
        value?.let {
            sendDataRemoved(key, it)
        }
        return value
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

    override fun getOrWait(key: K, time: Long, unit: TimeUnit): V? {
        var value: V? = get(key)
        if (value != null) {
            return value
        }

        val launch = CountDownLatch(1)
        val callback = object : OnDataChangedCallback<Map.Entry<K, V>>() {
            override fun onDataAdd(t: Map.Entry<K, V>) {
                super.onDataAdd(t)
                if (t.key == key) {
                    value = t.value
                    launch.countDown()
                }
            }
        }
        addDataChangedCallback(callback)
        value = get(key)
        if (value != null) {
            launch.countDown()
        }
        try {
            launch.await(time, unit)
        } catch (_: Throwable) {
        }
        removeDataChangedCallback(callback)
        return value
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
        val temp: Map<K, V>
        read.lock()
        try {
            temp = map.toMap()
        } finally {
            read.unlock()
        }
        temp.forEach {
            observer(it.key, it.value)
        }
    }

    override fun filterSync(predicate: (K, V) -> Boolean): Map<K, V> {
        val temp: Map<K, V>
        read.lock()
        try {
            temp = map.toMap()
        } finally {
            read.unlock()
        }
        return temp.filter {
            predicate(it.key, it.value)
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

    private fun sendDataAdd(key: K, value: V) {
        dataChangedCallbacks.forEachSync {
            it.onDataAdd(MapEntry(key, value))
        }
    }

    private fun sendDataRemoved(key: K, value: V) {
        dataChangedCallbacks.forEachSync {
            it.onDataRemoved(MapEntry(key, value))
        }
    }

    private class MapEntry<K, V>(override val key: K, override val value: V) : Map.Entry<K, V>
}