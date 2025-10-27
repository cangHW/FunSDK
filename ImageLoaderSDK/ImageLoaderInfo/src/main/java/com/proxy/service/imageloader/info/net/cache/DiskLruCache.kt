package com.proxy.service.imageloader.info.net.cache

import androidx.collection.LruCache
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.sp.CsSpManager
import com.proxy.service.core.framework.io.sp.SpMode
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import java.io.File
import java.util.Arrays

/**
 * @author: cangHX
 * @data: 2025/10/15 10:34
 * @desc:
 */
class DiskLruCache(
    private val dirName: String,
    cacheMaxSize: Int,
    spName: String
) {

    constructor(dirName: String) : this(
        dirName,
        ImageLoaderConstants.CACHE_MAX_SIZE,
        dirName
    )

    constructor(dirName: String, cacheMaxSize: Int) : this(
        dirName,
        cacheMaxSize,
        dirName
    )

    constructor(dirName: String, spName: String) : this(
        dirName,
        ImageLoaderConstants.CACHE_MAX_SIZE,
        spName
    )

    private val lru = LinkedHashMapImpl(cacheMaxSize, this)
    private val sp = CsSpManager.name(spName).mode(SpMode.MULTI_PROCESS_MODE).getController()

    /**
     * 从缓存中获取文件上次使用时间点
     * */
    private fun getTimeFromCache(map: HashMap<String, Long>, key: String): Long {
        var value = map.get(key)
        if (value == null) {
            value = sp.getLong(key)
            map.put(key, value)
        }
        return value
    }

    /**
     * 更新文件使用时间点
     * */
    private fun update(file: File, time: Long) {
        lru.put(file.name, CsFileUtils.length(file).toInt())
        sp.put(file.name, time)
    }

    /**
     * 获取文件夹 file
     * */
    private fun getDirOnly(): File {
        val cache = CsContextManager.getApplication().cacheDir
        return File(cache, dirName)
    }

    /**
     * 获取文件 file
     * */
    private fun getFileOnly(name: String): File {
        return File(getDirOnly(), name)
    }

    /**
     * 外部更新文件使用时间点
     * */
    fun update(name: String) {
        val file = getFile(name)
        if (CsFileUtils.isFile(file)) {
            update(file, System.currentTimeMillis())
        }
    }

    /**
     * 外部根据文件名获取文件 file
     * */
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

        val children = getDirOnly().list()
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
            CsLogger.tag(ImageLoaderConstants.TAG).d("DiskLruCache entryRemoved. evicted=$evicted, key=$key, oldValue=$oldValue, newValue=$newValue")
            if (evicted) {
                CsFileUtils.delete(cache.getFileOnly(key))
                cache.sp.removeValueForKey(key)
            }
        }
    }
}