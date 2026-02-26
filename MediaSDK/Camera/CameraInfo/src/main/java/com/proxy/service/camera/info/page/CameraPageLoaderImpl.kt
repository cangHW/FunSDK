package com.proxy.service.camera.info.page

import android.app.Application
import android.content.Context
import android.content.Intent
import com.proxy.service.camera.base.callback.PageTakePictureCallback
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.base.config.page.PageConfig
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
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
class CameraPageLoaderImpl(
    private val config: PageConfig
) : ICameraPageLoader {

    private val params = MediaCameraParams()

    override fun setOpenCameraMode(list: List<CameraMode>): ICameraPageLoader {
        params.supportCameraModes.clear()
        params.supportCameraModes.addAll(list)
        return this
    }

    override fun setDefaultCameraFaceMode(mode: CameraFaceMode): ICameraPageLoader {
        params.defaultCameraFaceMode = mode
        return this
    }

    override fun setTakePictureCallback(callback: PageTakePictureCallback): ICameraPageLoader {
        params.takePictureCallback = callback
        return this
    }

    override fun launch() {
        launch(CsContextManager.getApplication())
    }

    override fun launch(context: Context) {
        launch(context, CsMediaCameraActivity::class.java, params)
    }

    override fun launchPortrait() {
        launchPortrait(CsContextManager.getApplication())
    }

    override fun launchPortrait(context: Context) {
        launch(context, CsMediaCameraPortraitActivity::class.java, params)
    }

    override fun launchLandscape() {
        launchLandscape(CsContextManager.getApplication())
    }

    override fun launchLandscape(context: Context) {
        launch(context, CsMediaCameraLandscapeActivity::class.java, params)
    }

    private fun launch(
        context: Context,
        aClass: Class<out CsMediaCameraActivity>,
        params: MediaCameraParams
    ) {
        val intent = Intent(context, aClass)
        intent.putExtra(CsMediaCameraActivity.PARAMS, CameraCache.put(context, params))
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

}