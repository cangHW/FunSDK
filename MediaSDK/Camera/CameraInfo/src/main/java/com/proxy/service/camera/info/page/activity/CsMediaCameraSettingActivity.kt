package com.proxy.service.camera.info.page.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.info.R
import com.proxy.service.camera.info.databinding.CsCameraInfoPageActivitySettingBinding
import com.proxy.service.camera.info.page.cache.CameraSettingCache
import com.proxy.service.camera.info.page.dialog.CsMediaCameraDialog
import com.proxy.service.camera.info.page.view.CameraPageSettingItemView
import com.proxy.service.widget.info.base.CsBaseActivity

/**
 * @author: cangHX
 * @data: 2026/4/9 10:58
 * @desc:
 */
class CsMediaCameraSettingActivity : CsBaseActivity<CsCameraInfoPageActivitySettingBinding>() {

    override fun isStatusBarDarkModelEnable(): Boolean {
        return true
    }

    companion object {
        private const val PARAMS_CAMERA_ID = "cameraId"

        fun launch(context: Context, cameraId: String) {
            val intent = Intent(context, CsMediaCameraSettingActivity::class.java)
            intent.putExtra(PARAMS_CAMERA_ID, cameraId)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): CsCameraInfoPageActivitySettingBinding {
        return CsCameraInfoPageActivitySettingBinding.inflate(inflater)
    }

    override fun initData(intent: Intent?) {
        super.initData(intent)
        val cameraId = intent?.getStringExtra(PARAMS_CAMERA_ID)
        val faceMode = if (cameraId == CameraFaceMode.FaceBack.getCameraId()) {
            CameraFaceMode.FaceBack
        } else {
            CameraFaceMode.FaceFront
        }

        updatePictureConfig(faceMode)
        updateVideoConfig(faceMode)
        updateConfigConfig()
    }


    private fun updatePictureConfig(faceMode: CameraFaceMode) {
        val list = CameraSettingCache.getPictureCaptureAllSupportSize(faceMode)
        var currentSelected = CameraSettingCache.getPictureCaptureSize(faceMode)

        binding?.csMediaCameraSettingItemPhotoResolution?.show(
            CameraPageSettingItemView.builder()
                .setIcon(android.R.drawable.ic_menu_crop)
                .setTitle(R.string.cs_camera_info_page_setting_photo_ratio)
                .setContent(currentSelected.toFullSizeString())
                .build()
        )
        binding?.csMediaCameraSettingItemPhotoResolution?.setOnViewClickListener { view ->
            CsMediaCameraDialog()
                .setTitle(R.string.cs_camera_info_page_setting_photo_ratio)
                .setDataSize(list.size)
                .setDataBind { text, radio, position ->
                    val size = list[position]
                    text.text = size.toFullSizeString()
                    radio.isChecked = size == currentSelected
                }
                .setItemClick {
                    currentSelected = list[it]
                    view.updateContent(currentSelected.toFullSizeString())
                    CameraSettingCache.savaPictureCaptureSize(faceMode, currentSelected)
                }
                .show(this)
        }
    }

    private fun updateVideoConfig(faceMode: CameraFaceMode) {
        val list = CameraSettingCache.getVideoRecordAllSupportSize(faceMode)
        var currentSelected = CameraSettingCache.getVideoRecordSize(faceMode)

        binding?.csMediaCameraSettingItemVideoResolution?.show(
            CameraPageSettingItemView.builder()
                .setIcon(android.R.drawable.ic_menu_slideshow)
                .setTitle(R.string.cs_camera_info_page_setting_video_resolution)
                .setContent(currentSelected.toFullSizeString())
                .build()
        )
        binding?.csMediaCameraSettingItemVideoResolution?.setOnViewClickListener { view ->
            CsMediaCameraDialog()
                .setTitle(R.string.cs_camera_info_page_setting_video_resolution)
                .setDataSize(list.size)
                .setDataBind { text, radio, position ->
                    val size = list[position]
                    text.text = size.toFullSizeString()
                    radio.isChecked = size == currentSelected
                }
                .setItemClick {
                    currentSelected = list[it]
                    view.updateContent(currentSelected.toFullSizeString())
                    CameraSettingCache.savaVideoRecordSize(faceMode, currentSelected)
                }
                .show(this)
        }


        binding?.csMediaCameraSettingItemVideoFps?.show(
            CameraPageSettingItemView.builder()
                .setIcon(android.R.drawable.ic_menu_recent_history)
                .setTitle(R.string.cs_camera_info_page_setting_video_fps)
                .setContent(R.string.cs_camera_info_page_setting_value_60fps.toString())
                .build()
        )
    }

    private fun updateConfigConfig() {
        binding?.csMediaCameraSettingItemCommonGrid?.show(
            CameraPageSettingItemView.builder()
                .setIcon(android.R.drawable.ic_menu_sort_by_size)
                .setTitle(R.string.cs_camera_info_page_setting_grid)
                .setSelect(false)
                .build()
        )

        binding?.csMediaCameraSettingItemCommonLevel?.show(
            CameraPageSettingItemView.builder()
                .setIcon(android.R.drawable.ic_menu_compass)
                .setTitle(R.string.cs_camera_info_page_setting_level)
                .setSelect(false)
                .build()
        )

        binding?.csMediaCameraSettingItemCommonTimer?.show(
            CameraPageSettingItemView.builder()
                .setIcon(android.R.drawable.ic_menu_recent_history)
                .setTitle(R.string.cs_camera_info_page_setting_timer)
                .setContent(R.string.cs_camera_info_page_setting_value_off.toString())
                .build()
        )
    }
}