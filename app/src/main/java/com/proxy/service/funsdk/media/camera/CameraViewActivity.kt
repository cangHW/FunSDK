package com.proxy.service.funsdk.media.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.CameraViewMode
import com.proxy.service.camera.base.view.ICameraView
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityCameraViewBinding

/**
 * @author: cangHX
 * @data: 2026/3/30 16:48
 * @desc:
 */
class CameraViewActivity : BaseActivity<ActivityCameraViewBinding>() {

    private var cameraView: ICameraView? = null

    override fun getViewBinding(inflater: LayoutInflater): ActivityCameraViewBinding {
        return ActivityCameraViewBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()

        cameraView = CsMediaCamera.createViewLoader()
            ?.setCameraFaceMode(CameraFaceMode.FaceBack)
            ?.setLifecycleOwner(this)
//            ?.setViewMode(CameraViewMode.TEXTURE_VIEW)
            ?.createTo(binding?.cameraLayout)
    }

    override fun onClick(view: View) {

    }
}