package com.proxy.service.funsdk.media.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.api.CloudSystem
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.callback.PageTakePictureCallback
import com.proxy.service.camera.base.config.page.PageConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityCameraBinding

/**
 * @author: cangHX
 * @data: 2026/2/4 17:53
 * @desc:
 */
class CameraActivity : BaseActivity<ActivityCameraBinding>() {

    private val tag = "CameraActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CloudSystem.getService(CameraService::class.java)
            ?.createPageLoader(PageConfig.builder().build())
            ?.setTakePictureCallback(object : PageTakePictureCallback {
                override fun onTakePictureSuccess(path: String) {
                    CsLogger.tag(tag).i("onTakePictureSuccess. path=$path")
                }

                override fun onTakePictureFailed() {
                    CsLogger.tag(tag).e("onTakePictureFailed.")
                }
            })
            ?.launch(this)
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityCameraBinding {
        return ActivityCameraBinding.inflate(inflater)
    }

    override fun onClick(view: View) {

    }

}