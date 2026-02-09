package com.proxy.service.funsdk.media.camera

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import com.proxy.service.api.CloudSystem
import com.proxy.service.camera.base.CameraService
import com.proxy.service.camera.base.config.view.ViewConfig
import com.proxy.service.camera.base.mode.CameraFaceMode
import com.proxy.service.camera.base.mode.ViewMode
import com.proxy.service.camera.base.view.IView
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityCameraBinding
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2026/2/4 17:53
 * @desc:
 */
class CameraActivity : BaseActivity<ActivityCameraBinding>(), ActionCallback {

    private val service = CloudSystem.getService(CameraService::class.java)

    private var orientationListener: OrientationEventListener? = null

    private var iView: IView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orientationListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                // 检测设备方向变化
                when {
                    orientation in 45..135 -> {
                        // 横屏（逆时针旋转 90°）
                        CsToast.show("横屏 (逆时针)")
                    }
                    orientation in 225..315 -> {
                        // 横屏（顺时针旋转 90°）
                        CsToast.show("横屏 (顺时针)")
                    }
                    orientation in 0..45 || orientation in 315..360 -> {
                        // 竖屏
                        CsToast.show("竖屏")
                    }
                    orientation in 135..225 -> {
                        // 倒置竖屏
                        CsToast.show("倒置竖屏")
                    }
                }
            }
        }

        // 启动 OrientationEventListener
        if (orientationListener?.canDetectOrientation() == true) {
            orientationListener?.enable()
        } else {
            CsToast.show("无法检测方向变化")
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityCameraBinding {
        return ActivityCameraBinding.inflate(inflater)
    }

    override fun initView() {
        CsPermission.createRequest()
            ?.addPermission(Manifest.permission.CAMERA)
            ?.setGrantedCallback(this)
            ?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止 OrientationEventListener
        orientationListener?.disable()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.capturePhoto -> {
                iView?.capturePhoto(false, null)
            }
        }
    }

    override fun onAction(list: Array<String>) {
        val config = ViewConfig.builder()
            .setCameraFaceMode(CameraFaceMode.FaceBack)
            .setViewMode(ViewMode.TEXTURE_VIEW)
            .build()
        iView = service?.createViewLoader(config)
            ?.setLifecycleOwner(this)
            ?.createTo(binding?.preview)
    }

}