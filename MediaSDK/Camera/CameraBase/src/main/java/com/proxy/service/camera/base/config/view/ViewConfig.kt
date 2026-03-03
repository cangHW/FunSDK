package com.proxy.service.camera.base.config.view

import android.util.Size
import com.proxy.service.camera.base.config.view.builder.IBuilder
import com.proxy.service.camera.base.config.view.builder.IBuilderGet
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.base.mode.CameraViewAfMode
import com.proxy.service.camera.base.mode.ViewMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:12
 * @desc:
 */
class ViewConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet {

    override fun getAllUserSize(): List<UserSize> {
        return builder.getAllUserSize()
    }

    override fun getCameraFaceMode(): CameraFaceMode {
        return builder.getCameraFaceMode()
    }

    override fun getViewMode(): ViewMode {
        return builder.getViewMode()
    }

    override fun getCameraMode(): CameraMode {
        return builder.getCameraMode()
    }

    override fun getCameraViewAfMode(): CameraViewAfMode {
        return builder.getCameraViewAfMode()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private val userSize = ArrayList<UserSize>()

        private var cameraFaceMode = CameraConstants.DEFAULT_CAMERA_FACE_MODE
        private var viewMode = CameraConstants.DEFAULT_VIEW_MODE
        private var cameraMode = CameraConstants.DEFAULT_CAMERA_MODE
        private var cameraViewAfMode: CameraViewAfMode = CameraConstants.DEFAULT_CAMERA_VIEW_AF_MODE

        override fun setCameraPreviewSize(mode: CameraMode?, faceMode: CameraFaceMode?, size: Size): IBuilder {
            userSize.add(UserPreviewSize(mode, faceMode, size))
            return this
        }

        override fun setCameraOutSize(mode: CameraMode?, faceMode: CameraFaceMode?, size: Size): IBuilder {
            userSize.add(UserOutSize(mode, faceMode, size))
            return this
        }

        override fun setCameraFaceMode(mode: CameraFaceMode): IBuilder {
            this.cameraFaceMode = mode
            return this
        }

        override fun setViewMode(mode: ViewMode): IBuilder {
            this.viewMode = mode
            return this
        }

        override fun setCameraMode(mode: CameraMode): IBuilder {
            this.cameraMode = mode
            return this
        }

        override fun setCameraViewAfMode(mode: CameraViewAfMode): IBuilder {
            this.cameraViewAfMode = mode
            return this
        }

        override fun build(): ViewConfig {
            return ViewConfig(this)
        }

        override fun getAllUserSize(): List<UserSize> {
            return userSize
        }

        override fun getCameraFaceMode(): CameraFaceMode {
            return cameraFaceMode
        }

        override fun getViewMode(): ViewMode {
            return viewMode
        }

        override fun getCameraMode(): CameraMode {
            return cameraMode
        }

        override fun getCameraViewAfMode(): CameraViewAfMode {
            return cameraViewAfMode
        }
    }
}