package com.proxy.service.camera.info.loader

import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.MeteringRectangle
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.camera.base.config.loader.LoaderConfig
import com.proxy.service.camera.base.mode.CameraAfMode
import com.proxy.service.camera.base.mode.CameraMode
import com.proxy.service.camera.info.loader.lifecycle.LifecycleObserverImpl
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/2/4 16:19
 * @desc:
 */
class CameraLoaderImpl(
    config: LoaderConfig
) : AbstractCameraRecordLoader(config) {

    private var cameraAfMode: CameraAfMode = config.getCameraAfMode()


    override fun setLifecycleOwner(owner: LifecycleOwner) {
        CsLogger.tag(tag).i("setLifecycleOwner. owner=$owner")
        owner.lifecycle.addObserver(LifecycleObserverImpl(owner, this))
    }

    override fun setCameraAfMode(mode: CameraAfMode) {
        if (isReleased.get()) {
            CsLogger.tag(tag).w("The camera has been released.")
            return
        }
        CsLogger.tag(tag).i("setCameraAfMode. mode=$mode")

        cameraAfMode = mode
        resumePreview()
    }


    override fun parseCaptureRequestBuilder(builder: CaptureRequest.Builder) {
        super.parseCaptureRequestBuilder(builder)

        if (cameraAfMode == CameraAfMode.AfAutoMode) {
            val afMode: Int? = when (cameraMode) {
                CameraMode.CAPTURE -> {
                    CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                }

                CameraMode.RECORD -> {
                    CameraMetadata.CONTROL_AF_MODE_CONTINUOUS_VIDEO
                }

                else -> {
                    null
                }
            }
            afMode?.let {
                builder.set(CaptureRequest.CONTROL_AF_MODE, afMode)
            }
        } else if (cameraAfMode is CameraAfMode.AfFixedMode) {
            val list = ArrayList<MeteringRectangle>()
            (cameraAfMode as? CameraAfMode.AfFixedMode?)?.list?.forEach {
                val area = MeteringRectangle(
                    it.x.coerceAtLeast(0),
                    it.y.coerceAtLeast(0),
                    it.width,
                    it.height,
                    it.weight
                )
                list.add(area)
            }

            if (list.isNotEmpty()) {
                builder.set(CaptureRequest.CONTROL_AF_REGIONS, list.toTypedArray())
                builder.set(CaptureRequest.CONTROL_AF_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO)
                builder.set(
                    CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START
                )
            }
        }
    }

}