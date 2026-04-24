package com.proxy.service.camera.info.page.cache

import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/2/10 19:46
 * @desc:
 */
object CameraParamsCache {

    const val CAMERA_TOKEN = "camera_token"

    private const val TAG = "${CameraConstants.TAG}CameraParams"

    private const val DELAY_TIME = 5 * 1000L

    private val cacheMap = CsExcellentMap<String, MediaCameraParams>()

    fun put(params: MediaCameraParams): String {
        val token = UUID.randomUUID().toString()
        cacheMap.putSync(token, params)
        delayClear(token)
        return token
    }

    fun get(token: String?): MediaCameraParams? {
        if (token == null) {
            return null
        }
        return cacheMap.removeSync(token)
    }

    private fun delayClear(token: String) {
        CsTask.delay(DELAY_TIME, TimeUnit.MILLISECONDS)
            ?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    val params = cacheMap.removeSync(token) ?: return
                    CsLogger.tag(TAG)
                        .w("It hasn't been used for too long. I'm going to recycle it. callback=$params")
                }
            })?.start()
    }
}