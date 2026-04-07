package com.proxy.service.camera.info.loader.controller.func.record

import android.view.Surface
import com.proxy.service.camera.base.callback.loader.VideoRecordCallback
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.info.loader.controller.IFunController

/**
 * @author: cangHX
 * @data: 2026/3/23 18:35
 * @desc:
 */
class RecordControllerImpl: ICameraRecordController,
    com.proxy.service.camera.info.loader.controller.IFunController {
    override fun setVideoRecordSize(width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun startVideoRecording(isSavePhotoAlbum: Boolean, callback: VideoRecordCallback?) {
        TODO("Not yet implemented")
    }

    override fun finishVideoRecording() {
        TODO("Not yet implemented")
    }

    override fun getSurface(): Surface {
        TODO("Not yet implemented")
    }

    override fun setSurfaceChangedCallback(callback: com.proxy.service.camera.info.loader.controller.IFunController.SurfaceChangedCallback) {
        TODO("Not yet implemented")
    }

    override fun setParamsController(controller: com.proxy.service.camera.info.loader.controller.IFunController.IParamsController) {
        TODO("Not yet implemented")
    }

    override fun destroy() {
        TODO("Not yet implemented")
    }
}