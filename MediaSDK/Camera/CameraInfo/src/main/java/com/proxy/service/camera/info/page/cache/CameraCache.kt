package com.proxy.service.camera.info.page.cache

import android.content.Context
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.type.Type

/**
 * @author: cangHX
 * @data: 2026/2/10 19:46
 * @desc:
 */
object CameraCache {

    data class DataInfo(val code: Long, val params: MediaCameraParams)

    private val weakMap = CsExcellentMap<Any, MediaCameraParams>(Type.WEAK)
    private val cacheMap = CsExcellentMap<Context, DataInfo>(Type.WEAK)

    fun put(context: Context, params: MediaCameraParams): Long {
        val time = System.currentTimeMillis()
        cacheMap.putSync(context, DataInfo(time, params))
        return time
    }

    fun get(code: Long): MediaCameraParams? {
        var params: MediaCameraParams? = null
        cacheMap.forEachSync { context, data ->
            if (data.code == code){
                params = data.params
                weakMap.removeAsync(context)
                return@forEachSync
            }
        }
        return params
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