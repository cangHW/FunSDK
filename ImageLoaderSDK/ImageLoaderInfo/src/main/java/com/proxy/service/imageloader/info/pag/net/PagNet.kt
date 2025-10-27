package com.proxy.service.imageloader.info.pag.net

import com.proxy.service.imageloader.info.net.AbstractNet
import com.proxy.service.imageloader.info.net.RequestCallback
import com.proxy.service.imageloader.info.net.cache.DiskLruCache
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/10/24 17:43
 * @desc:
 */
class PagNet private constructor() : AbstractNet() {

    companion object {

        private const val CACHE_DIR_NAME = "pag_network_cache"
        private const val CACHE_SP_NAME = "pag_disk_lru_cache"

        private val _instance by lazy { PagNet() }

        fun request(key: String, url: String, callback: RequestCallback) {
            _instance.enqueue(key, url, callback)
        }
    }

    private var cache: DiskLruCache = DiskLruCache(CACHE_DIR_NAME, CACHE_SP_NAME)

    override fun getFileFromCache(key: String): File {
        return cache.getFile(key)
    }

    override fun updateInfoForCache(key: String) {
        cache.update(key)
    }
}