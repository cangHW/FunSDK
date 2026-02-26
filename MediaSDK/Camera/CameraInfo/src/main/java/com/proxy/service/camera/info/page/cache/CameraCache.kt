package com.proxy.service.camera.info.page.cache

import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.type.Type

/**
 * @author: cangHX
 * @data: 2026/2/10 19:46
 * @desc:
 */
object CameraCache {

    private val weakMap = CsExcellentMap<Any, MediaCameraParams>(Type.WEAK)

    fun put(any: Any, params: MediaCameraParams): Int {
        weakMap.putSync(any, params)
        return any.hashCode()
    }

    fun get(code: Int): MediaCameraParams? {
        var params: MediaCameraParams? = null
        weakMap.forEachSync { any, mediaCameraParams ->
            if (code == any.hashCode()) {
                params = mediaCameraParams
                weakMap.removeAsync(any)
                return@forEachSync
            }
        }
        return params
    }
}