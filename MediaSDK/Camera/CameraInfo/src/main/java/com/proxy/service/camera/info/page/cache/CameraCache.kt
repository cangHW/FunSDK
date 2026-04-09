package com.proxy.service.camera.info.page.cache

import android.content.Context
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.IConsumer
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/2/10 19:46
 * @desc:
 */
object CameraCache {

    private const val TAG = "${CameraConstants.TAG}CameraCache"

    private const val DELAY_TIME = 5 * 1000L

    private val cacheMap = CsExcellentMap<Int, MediaCameraParams>()

    fun put(context: Context, params: MediaCameraParams): Int {
        val hashCode = context.hashCode()
        cacheMap.putSync(hashCode, params)
        delayClear(hashCode)
        return hashCode
    }

    fun get(hashCode: Int): MediaCameraParams? {
        return cacheMap.removeSync(hashCode)
    }

    private fun delayClear(code: Int) {
        CsTask.delay(DELAY_TIME, TimeUnit.MILLISECONDS)
            ?.doOnNext(object : IConsumer<Long> {
                override fun accept(value: Long) {
                    val params = cacheMap.removeSync(code)
                    CsLogger.tag(TAG)
                        .w("It hasn't been used for too long. I'm going to recycle it. callback=${params?.pictureCaptureCallback}")
                }
            })
            ?.start()
    }
}