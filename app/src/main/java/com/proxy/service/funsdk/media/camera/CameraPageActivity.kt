package com.proxy.service.funsdk.media.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.camera.base.callback.loader.PictureCaptureCallback
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.media.CsMediaCamera
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityCameraPageBinding

/**
 * @author: cangHX
 * @data: 2026/2/4 17:53
 * @desc:
 */
class CameraPageActivity : BaseActivity<ActivityCameraPageBinding>() {

    private val tag = "CameraPageActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CsMediaCamera.createPageLoader()
//            ?.setDefaultCameraFaceMode(CameraFaceMode.FaceFront)
            ?.setPictureCaptureCallback(object : PictureCaptureCallback{
                override fun onPictureCaptureSuccess(filePath: String) {
                    CsLogger.tag(tag).i("onTakePictureSuccess. filePath=$filePath")
                }

                override fun onPictureCaptureFailed() {
                    CsLogger.tag(tag).e("onTakePictureFailed.")
                }
            })
//            ?.launch(this)
            ?.launchLandscape(this)
//            ?.launchPortrait(this)

    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityCameraPageBinding {
        return ActivityCameraPageBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()

       val list = CsMediaCamera.getSupportedRecordSizes(CameraFaceMode.FaceBack)
        CsLogger.e("list = ${CsJsonUtils.toJson(list)}")
    }

    override fun onClick(view: View) {

    }

}