package com.proxy.service.apihttp.info.request

import com.proxy.service.core.framework.collections.CsExcellentMap

/**
 * @author: cangHX
 * @date: 2026/5/11 10:56
 * @desc:
 */
object BaseUrlManager {

    private val urls = CsExcellentMap<String, String>()

    fun addBaseUrl(key: String, url: String) {
        urls.putSync(key, url)
    }

    fun getBaseUrl(key: String): String? {
        return urls.get(key)
    }

}