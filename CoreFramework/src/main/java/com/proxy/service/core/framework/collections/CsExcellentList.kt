package com.proxy.service.core.framework.collections

import com.proxy.service.core.framework.collections.base.IList
import com.proxy.service.core.framework.collections.callback.OnDataChangedCallback
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * 线程安全、支持异步操作 list
 *
 * @author: cangHX
 * @data: 2025/9/17 16:32
 * @desc:
 */
class CsExcellentList<V> : IList<V> {

    private val lock = ReentrantReadWriteLock()
    private val read = lock.readLock()
    private val write = lock.writeLock()

    private val dataChangedCallbacks = ArrayList<OnDataChangedCallback<V>>()
    private val list = ArrayList<V>()

    override fun addDataChangedCallback(callback: OnDataChangedCallback<V>) {
        synchronized(dataChangedCallbacks) {
            dataChangedCallbacks.add(callback)
        }
    }

    override fun size(): Int {
        read.lock()
        try {
            return list.size
        } finally {
            read.unlock()
        }
    }

    override fun clear() {
        write.lock()
        try {
            list.clear()
        }finally {
            write.unlock()
        }
    }

    override fun get(position: Int): V? {
        read.lock()
        try {
            return list.getOrNull(position)
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

    override fun containsKey(v: V): Boolean {
        read.lock()
        try {
            return list.contains(v)
        } finally {
            read.unlock()
        }
    }

    override fun removeDataChangedCallback(callback: OnDataChangedCallback<V>) {
        synchronized(dataChangedCallbacks) {
            dataChangedCallbacks.remove(callback)
        }
    }

    override fun filterAsync(predicate: (V) -> Boolean, observer: (V) -> Unit) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                filterSync(predicate).forEach {
                    observer(it)
                }
                return ""
            }
        })?.start()
    }

    override fun filterSync(predicate: (V) -> Boolean): List<V> {
        val temp: List<V>
        read.lock()
        try {
            temp = list.toList()
        } finally {
            read.unlock()
        }
        return temp.filter(predicate)
    }

    override fun forEachAsync(observer: (V) -> Unit) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                forEachSync(observer)
                return ""
            }
        })?.start()
    }

    override fun forEachSync(observer: (V) -> Unit) {
        val temp: List<V>
        read.lock()
        try {
            temp = list.toList()
        } finally {
            read.unlock()
        }
        temp.forEach {
            observer(it)
        }
    }

    override fun removeAsync(predicate: (V) -> Boolean) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                removeSync(predicate)
                return ""
            }
        })?.start()
    }

    override fun removeAsync(value: V) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                removeSync(value)
                return ""
            }
        })?.start()
    }

    override fun removeSync(predicate: (V) -> Boolean) {
        filterSync(predicate).forEach {
            removeSync(it)
        }
    }

    override fun removeSync(value: V) {
        val state: Boolean
        write.lock()
        try {
            state = list.remove(value)
        } finally {
            write.unlock()
        }
        if (state) {
            sendDataRemoved(value)
        }
    }

    override fun putAsync(value: V) {
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                putSync(value)
                return ""
            }
        })?.start()
    }

    override fun putSync(value: V) {
        val state: Boolean
        write.lock()
        try {
            state = list.add(value)
        } finally {
            write.unlock()
        }
        if (state) {
            sendDataAdd(value)
        }
    }

    private fun sendDataAdd(value: V) {
        synchronized(dataChangedCallbacks) {
            dataChangedCallbacks.forEach {
                it.onDataAdd(value)
            }
        }
    }

    private fun sendDataRemoved(value: V) {
        synchronized(dataChangedCallbacks) {
            dataChangedCallbacks.forEach {
                it.onDataRemoved(value)
            }
        }
    }
}