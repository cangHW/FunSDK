package com.proxy.service.camera.info.loader

import com.proxy.service.camera.base.callback.VideoRecordCallback
import com.proxy.service.camera.base.config.loader.LoaderConfig

/**
 * @author: cangHX
 * @data: 2026/3/3 15:49
 * @desc:
 */
abstract class AbstractCameraRecordLoader(
    config: LoaderConfig
) : AbstractCameraCaptureLoader(config) {

    override fun setVideoRecordSize(width: Int, height: Int) {

    }

    override fun startVideoRecording(isSavePhotoAlbum: Boolean, callback: VideoRecordCallback?) {

    }

    override fun finishVideoRecording() {

    }

}