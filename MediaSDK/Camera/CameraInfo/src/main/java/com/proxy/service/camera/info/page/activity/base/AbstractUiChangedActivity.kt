package com.proxy.service.camera.info.page.activity.base

import android.content.res.Configuration
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.base.mode.loader.VideoEncoderMode
import com.proxy.service.camera.info.page.cache.CameraSettingCache
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.screen.CsScreenUtils

/**
 * @author: cangHX
 * @data: 2026/4/23 20:21
 * @desc:
 */
abstract class AbstractUiChangedActivity : AbstractCameraActivity() {

    override fun changeCameraFace() {
        super.changeCameraFace()

        refreshUiSize()
    }

    override fun onResume() {
        super.onResume()
        refreshGridLine()
        refreshUiSize()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refreshUiSize()
    }

    protected fun refreshGridLine() {
        binding?.cameraGridLine?.setGridEnabled(CameraSettingCache.isGridEnabled())
    }

    protected fun refreshUiSize() {
        val cameraView = iCameraView ?: return
        val view = binding?.csMediaCamera ?: return
        val funMode = cameraFunMode ?: return
        val faceMode = cameraFaceMode

        val size = when (funMode) {
            CameraFunMode.CAPTURE -> {
                CameraSettingCache.getPictureCaptureSize(faceMode).apply {
                    cameraCaptureController?.setSurfaceSize(width, height)
                }
            }

            CameraFunMode.RECORD -> {
                CameraSettingCache.getVideoRecordSize(faceMode).apply {
                    cameraRecordController?.setSurfaceSize(width, height)
                    cameraRecordController?.setVideoFrameRate(frameRate)
                    cameraRecordController?.setVideoEncodingBitRate(bitrate)
                    cameraRecordController?.setVideoEncoder(VideoEncoderMode.value(encoder))
                }
            }

            else -> {
                CameraSettingCache.getPreviewSize(faceMode)
            }
        }
        cameraView.setPreviewSize(size.width, size.height)
        view.post {
            val screenInfo = CameraSettingCache.getScreenSize()
            val w = if (CsScreenUtils.isPortrait() == screenInfo.isPortrait) {
                screenInfo.screenWidth
            } else {
                screenInfo.screenHeight
            }

            val h = if (CsScreenUtils.isPortrait() == screenInfo.isPortrait) {
                screenInfo.screenHeight
            } else {
                screenInfo.screenWidth
            }

            var fW: Int
            var fH: Int

            if (CsScreenUtils.isPortrait()) {
                fW = w
                fH = (w * 1f / size.height * size.width).toInt()
                fH = h.coerceAtMost(fH)
            } else {
                fH = h
                fW = (h * 1f / size.height * size.width).toInt()
                fW = w.coerceAtMost(fW)
            }

            CsLogger.tag(getTag()).d("size=$size, w=$w, h=$h, finalW=$fW, finalH=$fH")

            val params = view.layoutParams

            if (params.width == fW && params.height == fH) {
                cameraView.startPreview()
            }

            params.width = fW
            params.height = fH
            view.layoutParams = params
        }
    }

}