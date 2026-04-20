package com.proxy.service.camera.info.loader.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.view.Surface
import com.proxy.service.camera.base.loader.camera.ICameraFun
import com.proxy.service.camera.base.loader.camera.ICameraFunGet
import com.proxy.service.camera.base.loader.controller.ICameraCaptureController
import com.proxy.service.camera.base.loader.controller.ICameraRecordController
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.base.mode.loader.CameraFunMode
import com.proxy.service.camera.info.loader.controller.IFunController
import com.proxy.service.camera.info.loader.controller.IFunController.IParamsController
import com.proxy.service.camera.info.loader.controller.IFunController.SurfaceChangedCallback
import com.proxy.service.camera.info.loader.controller.func.capture.CaptureControllerImpl
import com.proxy.service.camera.info.loader.controller.func.record.RecordControllerImpl
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/3/23 18:11
 * @desc:
 */
abstract class AbstractCameraFunController : AbstractCameraPreviewController(), ICameraFun,
    ICameraFunGet {

    protected var cameraFunMode: CameraFunMode? = null
    private var funController: IFunController? = null


    override fun getChooseMode(): CameraFunMode? {
        return cameraFunMode
    }

    override fun chooseCaptureMode(): ICameraCaptureController {
        CsLogger.tag(tag).i("chooseCaptureMode.")
        cameraFunMode = CameraFunMode.CAPTURE
        val controller = CaptureControllerImpl.create(
            cameraDeviceManager,
            captureSessionManager,
            handler
        )
        changedFunController(controller)
        return controller
    }

    override fun chooseRecordMode(): ICameraRecordController {
        CsLogger.tag(tag).i("chooseRecordMode.")
        cameraFunMode = CameraFunMode.RECORD
        val controller = RecordControllerImpl()
        changedFunController(controller)
        return controller
    }

    override fun findCaptureSessionSurfaces(list: ArrayList<Surface>) {
        super.findCaptureSessionSurfaces(list)

        funController?.getSurface()?.let {
            list.add(it)
        }
    }

    override fun onClear() {
        super.onClear()
        funController?.destroy()
        funController = null
    }

    private fun changedFunController(controller: IFunController) {
        funController?.destroy()
        funController = controller
        funController?.setSurfaceChangedCallback(object : SurfaceChangedCallback {
            override fun onSurfaceChanged() {
                captureSessionManager.closeCaptureSession()
                resumePreview()
            }

            override fun refreshPreview(
                templateType: Int,
                tempSurfaces: List<Surface>,
                success: () -> Unit,
                failed: () -> Unit
            ) {
                captureSessionManager.closeCaptureSession()
                requestPreview(templateType, tempSurfaces, success, failed)
            }
        })
        funController?.setParamsController(object : IParamsController {
            override fun getCameraFaceMode(): CameraFaceMode? {
                return cameraFaceMode
            }
        })
    }

}