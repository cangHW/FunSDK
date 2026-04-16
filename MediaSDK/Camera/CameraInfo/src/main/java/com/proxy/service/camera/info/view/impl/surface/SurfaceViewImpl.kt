package com.proxy.service.camera.info.view.impl.surface

import android.view.SurfaceHolder
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.info.view.base.AbstractCameraSurfaceView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.camera.info.view.view.CustomSurfaceView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils

/**
 * @author: cangHX
 * @data: 2026/2/5 20:14
 * @desc:
 */
class SurfaceViewImpl(
    config: CameraViewConfig,
    private val view: CustomSurfaceView
) : AbstractCameraSurfaceView(config), ICameraView {

    private var surfaceWidth: Int = 0
    private var surfaceHeight: Int = 0

    private var holder: SurfaceHolder? = null

    override fun init() {
        super.init()

        view.setOnSurfaceViewChangedListener(surfaceHolderCallback)
    }

    override fun startCameraPreview() {
        CsLogger.tag(tag).d("startCameraPreview.")
        val holder = holder ?: return

        val faceMode = cameraController?.getOpenedCameraMode() ?: return
        val previewSize = getCalculatePreviewSize(faceMode, surfaceWidth, surfaceHeight)
        if (previewSize == null) {
            CsLogger.tag(tag).e("没有合适的预览尺寸.")
            return
        }

        holder.setFixedSize(previewSize.width, previewSize.height)
        cameraController?.setPreviewSurface(holder.surface)
        cameraController?.startPreview()
    }

    private val surfaceHolderCallback = object : CustomSurfaceView.OnSurfaceViewChangedListener {
        private fun runOnTextureChange(holder: SurfaceHolder, width: Int, height: Int) {
            if (surfaceWidth != width || surfaceHeight != height) {
                surfaceWidth = width
                surfaceHeight = height
                this@SurfaceViewImpl.holder = holder
                rotation = CsScreenUtils.getScreenRotation()

                startCameraPreview()
            }
        }

        override fun surfaceCreated(holder: SurfaceHolder, width: Int, height: Int) {
            CsLogger.tag(tag).d("surfaceCreated. width=$width, height=$height")
            runOnTextureChange(holder, width, height)

            startCallScreenRotation()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            CsLogger.tag(tag).d("surfaceChanged. width=$width, height=$height")
        }

        override fun surfaceLayoutChanged(holder: SurfaceHolder, width: Int, height: Int) {
            CsLogger.tag(tag).d("surfaceLayoutChanged. width=$width, height=$height")

            runOnTextureChange(holder, width, height)
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            CsLogger.tag(tag).d("surfaceDestroyed")
            this@SurfaceViewImpl.holder = null
            surfaceWidth = 0
            surfaceHeight = 0
            rotation = null
            stopPreview()

            finishCallScreenRotation()
        }
    }
}