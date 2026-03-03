package com.proxy.service.camera.info.view.base

import android.graphics.RectF
import android.util.Size
import android.view.MotionEvent
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.config.view.UserOutSize
import com.proxy.service.camera.base.config.view.UserPreviewSize
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.af.FocusAreaInfo
import com.proxy.service.camera.info.view.manager.CalculateSizeManager
import com.proxy.service.camera.info.view.touch.mode.AfModeDispatch

/**
 * @author: cangHX
 * @data: 2026/2/5 17:45
 * @desc:
 */
abstract class AbstractCameraActionView(
    config: ViewConfig,
    private val owner: LifecycleOwner?
) : AbstractCameraView(config), AfModeDispatch.OnCameraAfIntercept {

    private val calculateSizeManager = CalculateSizeManager.create().apply {
        config.getAllUserSize().forEach {
            if (it is UserPreviewSize) {
                setUserPreviewSize(it.mode, it.faceMode, it.size)
            } else if (it is UserOutSize) {
                setUserOutSize(it.mode, it.faceMode, it.size)
            }
        }
    }

    /**
     * 初始化
     * */
    open fun init() {
        mCameraLoader.openCamera(mCameraFaceMode)
        owner?.let {
            mCameraLoader.setLifecycleOwner(it)
        }
    }

    /**
     * 检测是否触发手动对焦
     * */
    override fun onTouchAfIntercept(event: MotionEvent): RectF? {
        val mode = mCameraViewAfMode as? CameraViewAfMode.AfTouchMode? ?: return null

        val halfW = mode.width
        val halfH = mode.height

        val x = event.x - halfW
        val y = event.y - halfH

        val list = ArrayList<FocusAreaInfo>()
        list.add(
            FocusAreaInfo.create(
                x.toInt(),
                y.toInt(),
                mode.width,
                mode.height,
                FocusAreaInfo.WEIGHT_MAX
            )
        )
        mCameraLoader.setCameraAfMode(CameraAfMode.AfFixedMode(list))

        return RectF(x, y, event.x + halfW, event.y + halfH)
    }

    override fun setPreviewSize(width: Int, height: Int) {
        calculateSizeManager.setUserPreviewSize(mCameraMode, mCameraFaceMode, Size(width, height))
        startPreview()
    }

    override fun setPictureCaptureSize(width: Int, height: Int) {
        calculateSizeManager.setUserOutSize(mCameraMode, mCameraFaceMode, Size(width, height))
        super.setPictureCaptureSize(width, height)
    }


    protected fun getCalculatePreviewSize(width: Int, height: Int): Size? {
        return calculateSizeManager.getPreviewSize(
            mCameraService,
            mCameraMode,
            mCameraFaceMode,
            width,
            height
        )
    }

    protected fun getCalculateOutSize(width: Int, height: Int): Size? {
        return calculateSizeManager.getOutSize(
            mCameraService,
            mCameraMode,
            mCameraFaceMode,
            width,
            height
        )
    }

}