package com.proxy.service.camera.info.page

import android.content.Context
import com.proxy.service.camera.base.callback.PageTakePictureCallback
import com.proxy.service.camera.base.page.ICameraPageLoader
import com.proxy.service.camera.base.config.page.PageConfig
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

    override fun setCapturePhotoCallback(callback: PageTakePictureCallback): ICameraPageLoader {
        params.takePictureCallback = callback
        return this
    }

    override fun launch() {
        launch(CsContextManager.getApplication())
    }

    override fun launch(context: Context) {
        CsMediaCameraActivity.launch(context, params)
    }

}