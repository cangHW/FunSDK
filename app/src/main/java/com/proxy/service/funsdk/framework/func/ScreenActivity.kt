package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.callback.ScreenOrientationCallback
import com.proxy.service.core.framework.system.screen.callback.ScreenRotationCallback
import com.proxy.service.core.framework.system.screen.enums.OrientationEnum
import com.proxy.service.core.framework.system.screen.enums.RotationEnum
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkScreenBinding

/**
 * @author: cangHX
 * @data: 2024/10/18 18:25
 * @desc:
 */
class ScreenActivity : BaseActivity<ActivityFrameworkScreenBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ScreenActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun initView() {
        binding?.checkScreenMode?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsScreenUtils.addScreenOrientationCallback(orientationCallback)
            } else {
                CsScreenUtils.removeScreenOrientationCallback(orientationCallback)
            }
        }

        binding?.checkScreenRotation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CsScreenUtils.addScreenRotationCallback(rotationCallback)
            } else {
                CsScreenUtils.removeScreenRotationCallback(rotationCallback)
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.screen_width -> {
                val width = CsScreenUtils.getScreenWidth()
                val height = CsScreenUtils.getScreenHeight()

                binding?.content?.addData("屏幕", "宽高: $width x $height")
            }

            R.id.screen_real_size -> {
                val info = CsScreenUtils.getScreenRealInfo().toString()

                binding?.content?.addData("屏幕", "物理信息: $info")
            }

            R.id.status_bar_height -> {
                val height = CsScreenUtils.getStatusBarHeight()

                binding?.content?.addData("屏幕", "状态栏高度: $height")
            }

            R.id.navigation_bar_height -> {
                val height = CsScreenUtils.getNavigationBarHeight()

                binding?.content?.addData("屏幕", "导航栏高度: $height")
            }

            R.id.action_bar_height -> {
                val height = CsScreenUtils.getActionBarHeight()

                binding?.content?.addData("屏幕", "标题栏高度: $height")
            }

            R.id.screen_mode -> {
                if (CsScreenUtils.isPortrait()) {
                    binding?.content?.addData("屏幕", "当前竖屏")
                } else if (CsScreenUtils.isLandscape()) {
                    binding?.content?.addData("屏幕", "当前横屏")
                }
            }

            R.id.screen_rotation -> {
                val rotation = CsScreenUtils.getScreenRotation(this)

                binding?.content?.addData("屏幕", "旋转角度 ${rotation.name}")
            }
        }
    }

    private val orientationCallback = object : ScreenOrientationCallback {
        override fun onOrientationChange(orientation: OrientationEnum) {
            binding?.content?.addData("横竖屏切换", "$orientation")
        }
    }

    private val rotationCallback = object : ScreenRotationCallback {
        override fun onRotation(rotation: RotationEnum) {
            binding?.content?.addData("屏幕旋转", "$rotation")
        }
    }
}