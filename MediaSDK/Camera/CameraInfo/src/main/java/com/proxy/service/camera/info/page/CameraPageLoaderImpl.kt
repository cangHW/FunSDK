package com.proxy.service.camera.info.page

import android.app.Application
import android.content.Context
import android.content.Intent
import com.proxy.service.camera.base.callback.loader.PictureCaptureByteCallback
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.info.page.activity.CsMediaCameraActivity
import com.proxy.service.camera.info.page.activity.CsMediaCameraLandscapeActivity
import com.proxy.service.camera.info.page.activity.CsMediaCameraPortraitActivity
import com.proxy.service.camera.info.page.cache.CameraParamsCache
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2026/2/4 16:20
 * @desc:
 */
class CameraPageLoaderImpl : ICameraPageLoader {

    private val params = MediaCameraParams()

    override fun setSupportCameraFunMode(list: List<CameraFunMode>): ICameraPageLoader {
        params.supportCameraFunModes.clear()
        params.supportCameraFunModes.addAll(list)
        return this
    }

    override fun setDefaultCameraFaceMode(mode: CameraFaceMode): ICameraPageLoader {
        params.defaultCameraFaceMode = mode
        return this
    }


    override fun setPictureCaptureCallback(callback: PictureCaptureCallback): ICameraPageLoader {
        params.pictureCaptureParams.filePath = null
        params.pictureCaptureParams.isSaveAlbum = false
        params.pictureCaptureParams.pictureCaptureCallback = callback
        params.pictureCaptureParams.pictureCaptureByteCallback = null
        return this
    }

    override fun setPictureCaptureCallback(
        filePath: String,
        callback: PictureCaptureCallback
    ): ICameraPageLoader {
        params.pictureCaptureParams.filePath = filePath
        params.pictureCaptureParams.isSaveAlbum = false
        params.pictureCaptureParams.pictureCaptureCallback = callback
        params.pictureCaptureParams.pictureCaptureByteCallback = null
        return this
    }

    override fun setPictureCaptureCallback(callback: PictureCaptureByteCallback): ICameraPageLoader {
        params.pictureCaptureParams.filePath = null
        params.pictureCaptureParams.isSaveAlbum = false
        params.pictureCaptureParams.pictureCaptureCallback = null
        params.pictureCaptureParams.pictureCaptureByteCallback = callback
        return this
    }

    override fun setPictureCaptureToAlbumCallback(callback: PictureCaptureCallback): ICameraPageLoader {
        params.pictureCaptureParams.filePath = null
        params.pictureCaptureParams.isSaveAlbum = true
        params.pictureCaptureParams.pictureCaptureCallback = callback
        params.pictureCaptureParams.pictureCaptureByteCallback = null
        return this
    }


    override fun setVideoRecordCallback(callback: VideoRecordCallback): ICameraPageLoader {
        params.videoRecordParams.callback = callback
        params.videoRecordParams.filePath = null
        params.videoRecordParams.isSaveAlbum = false
        return this
    }

    override fun setVideoRecordCallback(
        filePath: String,
        callback: VideoRecordCallback
    ): ICameraPageLoader {
        params.videoRecordParams.callback = callback
        params.videoRecordParams.filePath = filePath
        params.videoRecordParams.isSaveAlbum = false
        return this
    }

    override fun setVideoRecordToAlbumCallback(callback: VideoRecordCallback): ICameraPageLoader {
        params.videoRecordParams.callback = callback
        params.videoRecordParams.filePath = null
        params.videoRecordParams.isSaveAlbum = true
        return this
    }


    override fun launch(context: Context?) {
        launch(context, CsMediaCameraActivity::class.java, params)
    }

    override fun launchPortrait(context: Context?) {
        launch(context, CsMediaCameraPortraitActivity::class.java, params)
    }

    override fun launchLandscape(context: Context?) {
        launch(context, CsMediaCameraLandscapeActivity::class.java, params)
    }

    private fun launch(
        context: Context?,
        aClass: Class<out CsMediaCameraActivity>,
        params: MediaCameraParams
    ) {
        val ctx = context ?: CsContextManager.getApplication()
        val intent = Intent(ctx, aClass)
        intent.putExtra(CsMediaCameraActivity.TOKEN, CameraParamsCache.put(params))
        if (ctx is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        ctx.startActivity(intent)
    }

}