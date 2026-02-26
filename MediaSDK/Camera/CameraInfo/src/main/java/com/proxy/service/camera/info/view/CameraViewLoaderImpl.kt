package com.proxy.service.camera.info.view

import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.TextureView
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.callback.ITouchDispatch
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.mode.ViewMode
import com.proxy.service.camera.base.view.ICameraViewLoader
import com.proxy.service.camera.base.view.IView
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.view.base.AbstractViewImpl
import com.proxy.service.camera.info.view.impl.EmptyViewImpl
import com.proxy.service.camera.info.view.impl.SurfaceViewImpl
import com.proxy.service.camera.info.view.impl.TextureViewImpl
import com.proxy.service.camera.info.view.touch.CameraTouchView

/**
 * @author: cangHX
 * @data: 2026/2/4 16:20
 * @desc:
 */
class CameraViewLoaderImpl(
    private val config: ViewConfig
) : ICameraViewLoader {

    private var lifecycleOwner: LifecycleOwner? = null
    private var touchDispatch: ITouchDispatch? = null

    override fun setLifecycleOwner(owner: LifecycleOwner): ICameraViewLoader {
        this.lifecycleOwner = owner
        return this
    }

    override fun setCustomTouchDispatch(touchDispatch: ITouchDispatch): ICameraViewLoader {
        this.touchDispatch = touchDispatch
        return this
    }

    override fun createTo(viewGroup: ViewGroup?): IView {
        if (viewGroup == null) {
            return EmptyViewImpl()
        }

        val iView: AbstractViewImpl? = if (config.getViewMode() == ViewMode.TEXTURE_VIEW) {
            createByTextureView(viewGroup)
        } else if (config.getViewMode() == ViewMode.SURFACE_VIEW) {
            createBySurfaceView(viewGroup)
        } else {
            null
        }

        iView?.init()
        return iView ?: EmptyViewImpl()
    }

    private fun createByTextureView(viewGroup: ViewGroup): AbstractViewImpl {
        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cs_camera_info_view_texture, viewGroup, true)
        val textureView = rootView.findViewById<TextureView>(R.id.view_texture)
        val viewImpl = TextureViewImpl(config, lifecycleOwner, textureView)

        val cameraTouchView = rootView.findViewById<CameraTouchView>(R.id.view_touch)
        cameraTouchView.setCustomTouchDispatch(touchDispatch)
        cameraTouchView.setOnCameraAfIntercept(viewImpl)
        return viewImpl
    }

    private fun createBySurfaceView(viewGroup: ViewGroup): AbstractViewImpl {
        val rootView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.cs_camera_info_view_surface, viewGroup, true)
        val surfaceView = rootView.findViewById<SurfaceView>(R.id.view_surface)
        val viewImpl = SurfaceViewImpl(config, lifecycleOwner, surfaceView)

        val cameraTouchView = rootView.findViewById<CameraTouchView>(R.id.view_touch)
        cameraTouchView.setCustomTouchDispatch(touchDispatch)
        cameraTouchView.setOnCameraAfIntercept(viewImpl)
        return viewImpl
    }
}