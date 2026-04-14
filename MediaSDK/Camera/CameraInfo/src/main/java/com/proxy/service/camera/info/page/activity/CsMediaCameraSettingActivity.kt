package com.proxy.service.camera.info.page.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.proxy.service.camera.info.databinding.CsCameraInfoPageActivitySettingBinding
import com.proxy.service.widget.info.base.CsBaseActivity

/**
 * @author: cangHX
 * @data: 2026/4/9 10:58
 * @desc:
 */
class CsMediaCameraSettingActivity : CsBaseActivity<CsCameraInfoPageActivitySettingBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CsMediaCameraSettingActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): CsCameraInfoPageActivitySettingBinding {
        return CsCameraInfoPageActivitySettingBinding.inflate(inflater)
    }
}