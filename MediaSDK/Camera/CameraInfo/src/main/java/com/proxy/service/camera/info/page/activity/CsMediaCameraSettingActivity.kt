package com.proxy.service.camera.info.page.activity

import android.Manifest
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
import com.proxy.service.core.service.permission.CsPermission
import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.widget.info.base.CsBaseActivity
import com.proxy.service.widget.info.toast.CsToast

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
        updateGeneralConfig()
    }


    private fun updatePictureConfig(faceMode: CameraFaceMode) {
        val list = CameraSettingCache.getPictureCaptureAllSupportSize(faceMode)
        var currentSelected = CameraSettingCache.getPictureCaptureSize(faceMode)

        binding?.csCameraInfoSettingItemPhotoRatio?.show(
            CameraPageSettingItemView.builder()
                .setIcon(R.drawable.cs_camera_info_setting_icon_photo_ratio)
                .setTitle(R.string.cs_camera_info_page_setting_photo_ratio)
                .setContent(currentSelected.toFullSizeString())
                .build()
        )
        binding?.csCameraInfoSettingItemPhotoRatio?.setOnViewClickListener { view ->
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
        val list = CameraSettingCache.getVideoRecordAllSupportInfos(faceMode)
        var currentSelected = CameraSettingCache.getVideoRecordInfo(faceMode)

        binding?.csCameraInfoSettingItemVideoResolution?.show(
            CameraPageSettingItemView.builder()
                .setIcon(R.drawable.cs_camera_info_setting_icon_video_resolution)
                .setTitle(R.string.cs_camera_info_page_setting_video_resolution)
                .setContent(currentSelected.toFullSizeString())
                .build()
        )
        binding?.csCameraInfoSettingItemVideoResolution?.setOnViewClickListener { view ->
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
                    CameraSettingCache.savaVideoRecordInfo(faceMode, currentSelected)
                }
                .show(this)
        }

        binding?.csCameraInfoSettingItemVideoAudio?.show(
            CameraPageSettingItemView.builder()
                .setIcon(R.drawable.cs_camera_info_setting_icon_video_audio)
                .setTitle(R.string.cs_camera_info_page_setting_video_audio)
                .setSelect(CameraSettingCache.isAudioEnable())
                .build()
        )
        binding?.csCameraInfoSettingItemVideoAudio?.setOnViewSelectListener {
            CameraSettingCache.saveAudioEnable(it)

            if (it && !CsPermission.isPermissionGranted(Manifest.permission.RECORD_AUDIO)) {
                CsPermission.createAutoRequest()
                    ?.addPermission(Manifest.permission.RECORD_AUDIO)
                    ?.setTitle(getString(R.string.cs_camera_info_page_camera_no_permission))
                    ?.setDialogSettingContent(getString(R.string.cs_camera_info_page_camera_request_audio_permission))
                    ?.setDialogRationaleContent(getString(R.string.cs_camera_info_page_camera_audio_permission_prompt))
                    ?.setDeniedCallback(object : ActionCallback {
                        override fun onAction(list: Array<String>) {
                            CsToast.show(R.string.cs_camera_info_page_camera_no_audio_permission_toast)
                            binding?.csCameraInfoSettingItemVideoAudio?.updateChecked(false)
                        }
                    })
                    ?.start(this)
            }
        }
    }

    private fun updateGeneralConfig() {
        binding?.csCameraInfoSettingItemGeneralGrid?.show(
            CameraPageSettingItemView.builder()
                .setIcon(R.drawable.cs_camera_info_setting_icon_grid)
                .setTitle(R.string.cs_camera_info_page_setting_grid)
                .setSelect(CameraSettingCache.isGridEnabled())
                .build()
        )
        binding?.csCameraInfoSettingItemGeneralGrid?.setOnViewSelectListener {
            CameraSettingCache.saveGridEnabled(it)
        }

        binding?.csCameraInfoSettingItemGeneralLevel?.show(
            CameraPageSettingItemView.builder()
                .setIcon(R.drawable.cs_camera_info_setting_icon_level)
                .setTitle(R.string.cs_camera_info_page_setting_level)
                .setSelect(CameraSettingCache.isLevelEnabled())
                .build()
        )
        binding?.csCameraInfoSettingItemGeneralLevel?.setOnViewSelectListener {
            CameraSettingCache.saveLevelEnabled(it)
        }
    }
}