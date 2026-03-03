package com.proxy.service.camera.base.config.loader

import com.proxy.service.camera.base.config.loader.builder.IBuilder
import com.proxy.service.camera.base.config.loader.builder.IBuilderGet
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:11
 * @desc:
 */
class LoaderConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet {

    override fun getCameraMode(): CameraMode? {
        return builder.getCameraMode()
    }

    override fun getCameraAfMode(): CameraAfMode {
        return builder.getCameraAfMode()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var cameraMode: CameraMode? = null
        private var cameraAfMode: CameraAfMode = CameraConstants.DEFAULT_CAMERA_AF_MODE

        override fun setCameraMode(mode: CameraMode): IBuilder {
            this.cameraMode = mode
            return this
        }

        override fun setCameraAfMode(mode: CameraAfMode): IBuilder {
            this.cameraAfMode = mode
            return this
        }

        override fun build(): LoaderConfig {
            return LoaderConfig(this)
        }

        override fun getCameraMode(): CameraMode? {
            return cameraMode
        }

        override fun getCameraAfMode(): CameraAfMode {
            return cameraAfMode
        }

    }

}