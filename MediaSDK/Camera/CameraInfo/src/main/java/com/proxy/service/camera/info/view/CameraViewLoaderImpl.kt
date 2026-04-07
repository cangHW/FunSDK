package com.proxy.service.camera.info.view

import android.util.Size
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.callback.view.ITouchDispatch
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraFunMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.CameraViewMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.camera.base.view.ICameraViewLoader
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.view.base.AbstractCameraSurfaceView
import com.proxy.service.camera.info.view.config.CameraViewConfig
import com.proxy.service.camera.info.view.config.UserOutSize
import com.proxy.service.camera.info.view.config.UserPreviewSize
import com.proxy.service.camera.info.view.impl.EmptyCameraViewImpl
import com.proxy.service.camera.info.view.impl.surface.SurfaceViewImpl
import com.proxy.service.camera.info.view.impl.surface.TextureViewImpl
import com.proxy.service.camera.info.view.touch.CameraTouchView

/**
 * @author: cangHX
 * @data: 2026/2/4 16:20
 * @desc:
 */
class CameraViewLoaderImpl : ICameraViewLoader {

    private val config = CameraViewConfig()
    private var touchDispatch: ITouchDispatch? = null
    private var viewMode = CameraConstants.DEFAULT_VIEW_MODE

    override fun setLifecycleOwner(owner: LifecycleOwner): ICameraViewLoader {
        config.lifecycleOwner = owner
        return this
    }

    override fun setCustomTouchDispatch(touchDispatch: ITouchDispatch): ICameraViewLoader {
        this.touchDispatch = touchDispatch
        return this
    }

    override fun setCameraPreviewSize(
        mode: CameraFunMode?,
        faceMode: CameraFaceMode?,
        size: Size
    ): ICameraViewLoader {
        config.userSize.add(UserPreviewSize(mode, faceMode, size))
        return this
    }

    override fun setCameraOutSize(
        mode: CameraFunMode?,
        faceMode: CameraFaceMode?,
        size: Size
    ): ICameraViewLoader {
        config.userSize.add(UserOutSize(mode, faceMode, size))
        return this
    }

    override fun setCameraFaceMode(mode: CameraFaceMode): ICameraViewLoader {
        config.cameraFaceMode = mode
        return this
    }

    override fun setCameraViewMode(mode: CameraViewMode): ICameraViewLoader {
        viewMode = mode
        return this
    }

    override fun setCameraViewAfMode(mode: CameraViewAfMode): ICameraViewLoader {
        config.cameraViewAfMode = mode
        return this
    }

    override fun createTo(viewGroup: ViewGroup?): ICameraView {
        if (viewGroup == null) {
            return EmptyCameraViewImpl()
        }

        val iView: AbstractCameraSurfaceView? = when (viewMode) {
            CameraViewMode.TEXTURE_VIEW -> {
                createByTextureView(viewGroup)
            }
            CameraViewMode.SURFACE_VIEW -> {
                createBySurfaceView(viewGroup)
            }
            else -> {
                null
            }
        }

        iView?.init()
        return iView ?: EmptyCameraViewImpl()
    }

    private fun createByTextureView(viewGroup: ViewGroup): AbstractCameraSurfaceView {
        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cs_camera_info_view_texture, viewGroup, true)
        val textureView = rootView.findViewById<TextureView>(R.id.view_texture)
        val viewImpl = TextureViewImpl(config, textureView)

        val cameraTouchView = rootView.findViewById<CameraTouchView>(R.id.view_touch)
        cameraTouchView.setCustomTouchDispatch(touchDispatch)
        cameraTouchView.setOnCameraAfIntercept(viewImpl)
        return viewImpl
    }

    private fun createBySurfaceView(viewGroup: ViewGroup): AbstractCameraSurfaceView {
        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cs_camera_info_view_surface, viewGroup, true)
        val surfaceView = rootView.findViewById<SurfaceView>(R.id.view_surface)
        val viewImpl = SurfaceViewImpl(config, surfaceView)

        val cameraTouchView = rootView.findViewById<CameraTouchView>(R.id.view_touch)
        cameraTouchView.setCustomTouchDispatch(touchDispatch)
        cameraTouchView.setOnCameraAfIntercept(viewImpl)
        return viewImpl
    }
}