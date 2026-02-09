package com.proxy.service.funsdk.media

import android.view.LayoutInflater
import android.view.View
import com.proxy.service.api.CloudSystem
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.callback.PageTakePictureCallback
import com.proxy.service.camera.base.config.page.PageConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.base.LaunchUtils
import com.proxy.service.funsdk.databinding.ActivityMediaBinding
import com.proxy.service.funsdk.media.camera.CameraActivity

/**
 * @author: cangHX
 * @data: 2026/2/4 17:52
 * @desc:
 */
class MediaActivity : BaseActivity<ActivityMediaBinding>() {

    override fun getViewBinding(inflater: LayoutInflater): ActivityMediaBinding {
        return ActivityMediaBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.media_camera -> {
//                LaunchUtils.launch(this, CameraActivity::class.java)
                CloudSystem.getService(CameraService::class.java)
                    ?.createPageLoader(PageConfig.builder().build())
                    ?.setCapturePhotoCallback(object : PageTakePictureCallback{
                        override fun onTakePictureSuccess(path: String) {
                            CsLogger.tag("MediaActivity").i("onTakePictureSuccess. path=$path")
                        }

                        override fun onTakePictureFailed() {
                            CsLogger.tag("MediaActivity").e("onTakePictureFailed.")
                        }
                    })
                    ?.launch(this)
            }
        }
    }
}