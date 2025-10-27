package com.proxy.service.imageloader.info.lottie.net

import com.airbnb.lottie.network.FileExtension
import com.airbnb.lottie.utils.Logger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.info.net.AbstractNet
import com.proxy.service.imageloader.info.net.RequestCallback
import com.proxy.service.imageloader.info.net.cache.DiskLruCache
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/10/24 17:43
 * @desc:
 */
class LottieNet private constructor() : AbstractNet() {

    companion object {

        private const val CACHE_DIR_NAME = "lottie_network_cache"
        private const val CACHE_SP_NAME = "lottie_disk_lru_cache"

        private val _instance by lazy { LottieNet() }

        fun request(key: String, url: String, callback: RequestCallback) {
            _instance.enqueue(key, url, callback)
        }
    }

    private var cache: DiskLruCache = DiskLruCache(CACHE_DIR_NAME, CACHE_SP_NAME)

    override fun createLocalFileName(key: String, url: String, contentType: String?): String {
        if (contentType == null) {
            return key + ImageLoaderConstants.LOTTIE_TYPE_JSON
        }

        val isUrlEndsWithLottie = url.split("\\?".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray().get(0)
            .endsWith(".lottie")

        if (
            contentType.contains("application/zip") ||
            contentType.contains("application/x-zip") ||
            contentType.contains("application/x-zip-compressed") ||
            isUrlEndsWithLottie
        ) {
            return key + ImageLoaderConstants.LOTTIE_TYPE_ZIP
        }
        return key + ImageLoaderConstants.LOTTIE_TYPE_JSON
    }

    override fun getFileFromCache(key: String): File {
        val jFile = cache.getFile("$key${ImageLoaderConstants.LOTTIE_TYPE_JSON}")
        if (CsFileUtils.isFile(jFile)) {
            return jFile
        }

        val zFile = cache.getFile("$key${ImageLoaderConstants.LOTTIE_TYPE_ZIP}")
        if (CsFileUtils.isFile(zFile)) {
            return zFile
        }

        return cache.getFile(key)
    }

    override fun updateInfoForCache(key: String) {
        cache.update(key)
    }
}