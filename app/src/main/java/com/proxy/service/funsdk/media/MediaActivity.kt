package com.proxy.service.funsdk.media

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.base.LaunchUtils
import com.proxy.service.funsdk.databinding.ActivityMediaBinding
import com.proxy.service.funsdk.media.camera.CameraLoaderActivity
import com.proxy.service.funsdk.media.camera.CameraPageActivity
import com.proxy.service.funsdk.media.camera.CameraViewActivity

/**
 * @author: cangHX
 * @data: 2026/2/4 17:52
 * @desc:
 */
class MediaActivity : BaseActivity<ActivityMediaBinding>() {

    override fun getViewBinding(inflater: LayoutInflater): ActivityMediaBinding {
        return ActivityMediaBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()

        CsPermission.createRequest()
            ?.addPermission(Manifest.permission.CAMERA)
            ?.start()

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.media_camera_loader -> {
                LaunchUtils.launch(this, CameraLoaderActivity::class.java)
            }

            R.id.media_camera_view -> {
                LaunchUtils.launch(this, CameraViewActivity::class.java)
            }

            R.id.media_camera_page -> {
                LaunchUtils.launch(this, CameraPageActivity::class.java)
            }
        }
    }
}