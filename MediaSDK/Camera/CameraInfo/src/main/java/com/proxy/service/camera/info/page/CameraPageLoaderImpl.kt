package com.proxy.service.camera.info.page

import android.app.Application
import android.content.Context
import android.content.Intent
import com.proxy.service.camera.base.callback.PagePictureCaptureCallback
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.base.config.page.PageConfig
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.info.page.activity.CsMediaCameraActivity
import com.proxy.service.camera.info.page.activity.CsMediaCameraLandscapeActivity
import com.proxy.service.camera.info.page.activity.CsMediaCameraPortraitActivity
import com.proxy.service.camera.info.page.cache.CameraCache
import com.proxy.service.camera.info.page.params.MediaCameraParams
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2026/2/4 16:20
 * @desc:
 */
class CameraPageLoaderImpl : ICameraPageLoader {

    private val params = MediaCameraParams()

    override fun setOpenCameraMode(list: List<CameraFunMode>): ICameraPageLoader {
        params.supportCameraFunModes.clear()
        params.supportCameraFunModes.addAll(list)
        return this
    }

    override fun setDefaultCameraFaceMode(mode: CameraFaceMode): ICameraPageLoader {
        params.defaultCameraFaceMode = mode
        return this
    }

    override fun setTakePictureCallback(callback: PagePictureCaptureCallback): ICameraPageLoader {
        params.pictureCaptureCallback = callback
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
        val intent = Intent(context, aClass)
        intent.putExtra(CsMediaCameraActivity.PARAMS, CameraCache.put(ctx, params))
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        ctx.startActivity(intent)
    }

}