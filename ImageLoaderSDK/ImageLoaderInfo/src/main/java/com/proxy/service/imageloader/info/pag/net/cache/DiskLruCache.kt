package com.proxy.service.imageloader.info.pag.net.cache

import androidx.collection.LruCache
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.sp.CsSpManager
import com.proxy.service.core.framework.io.sp.SpMode
import java.io.File
import java.util.Arrays

/**
 * @author: cangHX
 * @data: 2025/10/15 10:34
 * @desc:
 */
class DiskLruCache {

    companion object {
        private const val DIR_NAME = "pag_network_cache"
        private const val CACHE_MAX_SIZE = 250 * 1024 * 1024
        private const val SP_NAME = "pag_disk_lru_cache"
    }

    private val lru = LinkedHashMapImpl(CACHE_MAX_SIZE, this)
    private val sp = CsSpManager.name(SP_NAME).mode(SpMode.MULTI_PROCESS_MODE).getController()

    private fun getTimeFromCache(map: HashMap<String, Long>, key: String): Long {
        var value = map.get(key)
        if (value == null) {
            value = sp.getLong(key)
            map.put(key, value)
        }
        return value
    }

    private fun update(file: File, time: Long) {
        lru.put(file.name, CsFileUtils.length(file).toInt())
        sp.put(file.name, time)
    }

    private fun getDirFile(): File {
        val cache = CsContextManager.getApplication().cacheDir
        return File(cache, DIR_NAME)
    }

    private fun getFileOnly(name: String): File {
        return File(getDirFile(), name)
    }


    fun update(name: String) {
        val file = getFile(name)
        if (CsFileUtils.isFile(file)) {
            update(file, System.currentTimeMillis())
        }
    }

    fun getFile(name: String): File {
        val file = getFileOnly(name)
        if (lru.get(name) != null) {
            sp.put(name, System.currentTimeMillis())
        } else if (CsFileUtils.isFile(file)) {
            update(file, System.currentTimeMillis())
        }
        return file
    }

    init {
        val temp = HashMap<String, Long>()
        val array = sp.allKeys()

        val children = getDirFile().list()
        if (children != null && children.size > array.size) {
            children.filter {
                !array.contains(it)
            }.forEach {
                update(File(it), 0)
            }
        }

        Arrays.sort(array, object : Comparator<String> {
            override fun compare(o1: String?, o2: String?): Int {
                if (o1 == null || o2 == null) {
                    return 0
                }
                if (getTimeFromCache(temp, o1) > getTimeFromCache(temp, o2)) {
                    return 1
                }
                return -1
            }
        })
        array.forEach {
            lru.put(it, CsFileUtils.length(getFileOnly(it)).toInt())
        }
    }


    private class LinkedHashMapImpl(
        maxSize: Int,
        private val cache: DiskLruCache
    ) : LruCache<String, Int>(maxSize) {
        override fun sizeOf(key: String, value: Int): Int {
            return value
        }

        override fun entryRemoved(evicted: Boolean, key: String, oldValue: Int, newValue: Int?) {
            if (evicted) {
                CsFileUtils.delete(cache.getFileOnly(key))
                cache.sp.removeValueForKey(key)
            }
        }
    }
}