package com.proxy.service.camera.base.config.loader

import com.proxy.service.camera.base.config.loader.builder.IBuilder
import com.proxy.service.camera.base.config.loader.builder.IBuilderGet
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.mode.CameraAfMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:11
 * @desc:
 */
class LoaderConfig private constructor(
    private val builder: IBuilderGet
) : IBuilderGet {

    override fun getCameraAfMode(): CameraAfMode {
        return builder.getCameraAfMode()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {

        private var cameraAfMode: CameraAfMode = CameraConstants.DEFAULT_CAMERA_AF_MODE

        override fun setCameraAfMode(mode: CameraAfMode): IBuilder {
            this.cameraAfMode = mode
            return this
        }

        override fun build(): LoaderConfig {
            return LoaderConfig(this)
        }

        override fun getCameraAfMode(): CameraAfMode {
            return cameraAfMode
        }

    }

}