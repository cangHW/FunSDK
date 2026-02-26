package com.proxy.service.funsdk.media

import android.view.LayoutInflater
import android.view.View
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
                LaunchUtils.launch(this, CameraActivity::class.java)
            }
        }
    }
}