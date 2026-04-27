package com.proxy.service.camera.info.loader.factory

import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity.CAMERA_SERVICE
import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.info.AudioParamsInfo
import com.proxy.service.camera.base.loader.info.SupportSize
import com.proxy.service.camera.base.loader.info.VideoParamsInfo
import com.proxy.service.camera.base.loader.info.VideoSupportInfo
import com.proxy.service.camera.base.mode.loader.VideoPatternMode
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.Collections
import java.util.Objects

/**
 * @author: cangHX
 * @data: 2026/2/5 14:50
 * @desc:
 */
object CameraFactory {

    private const val TAG = "${CameraConstants.TAG}Factory"

    private val manager by lazy {
        CsContextManager.getApplication().getSystemService(CAMERA_SERVICE) as? CameraManager?
    }

    private var faceBackId: String? = null
    private var faceFrontId: String? = null

    private val supportedPreviewSizesMap = HashMap<String, List<SupportSize>>()
    private val supportedCaptureSizesMap = HashMap<String, List<SupportSize>>()
    private val supportedRecordSizesMap = HashMap<String, List<SupportSize>>()
    private val recommendPreviewSizesMap = HashMap<String, List<VideoSupportInfo>>()

    /**
     * 相机管理类
     * */
    fun getCameraManager(): CameraManager? {
        return manager
    }

    /**
     * 后置摄像头 ID
     * */
    fun getCameraFaceBackId(): String? {
        if (faceBackId == null) {
            checkSupportCameraId()
        }
        return faceBackId
    }

    /**
     * 前置摄像头 ID
     * */
    fun getCameraFaceFrontId(): String? {
        if (faceFrontId == null) {
            checkSupportCameraId()
        }
        return faceFrontId
    }

    /**
     * 获取摄像头支持的预览尺寸
     * */
    fun getSupportedPreviewSizes(cameraId: String): List<SupportSize>? {
        val size = supportedPreviewSizesMap[cameraId]
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(SurfaceTexture::class.java)

        val sList = ArrayList<SupportSize>()
        supportedSizes?.forEach {
            sList.add(SupportSize.create(it.width, it.height))
        }
        if (sList.isEmpty()) {
            return null
        }
        supportedPreviewSizesMap.put(cameraId, sList)
        return sList
    }

    /**
     * 获取摄像头支持的拍照尺寸
     * */
    fun getSupportedCaptureSizes(cameraId: String): List<SupportSize>? {
        val size = supportedCaptureSizesMap[cameraId]
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(ImageFormat.JPEG)

        val sList = ArrayList<SupportSize>()
        supportedSizes?.forEach {
            sList.add(SupportSize.create(it.width, it.height))
        }
        if (sList.isEmpty()) {
            return null
        }
        supportedCaptureSizesMap.put(cameraId, sList)
        return sList
    }

    /**
     * 获取摄像头支持的视频尺寸
     * */
    fun getSupportedRecordSizes(cameraId: String): List<SupportSize>? {
        val size = supportedRecordSizesMap[cameraId]
        if (size != null) {
            return size
        }
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val supportedSizes = map?.getOutputSizes(MediaRecorder::class.java)

        val sList = ArrayList<SupportSize>()
        supportedSizes?.forEach {
            sList.add(SupportSize.create(it.width, it.height))
        }
        if (sList.isEmpty()) {
            return null
        }
        supportedRecordSizesMap.put(cameraId, sList)
        return sList
    }

    /**
     * 获取推荐的录制视频参数
     * */
    fun getRecommendRecordSizes(
        cameraId: String,
        pattern: VideoPatternMode
    ): List<VideoSupportInfo>? {
        val key = "${cameraId}_${pattern.name}"
        val size = recommendPreviewSizesMap[key]
        if (size != null) {
            return size
        }

        val sList = ArrayList<VideoSupportInfo>()
        when (pattern) {
            VideoPatternMode.NORMAL -> {
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH)?.let {
                    sList.addAll(it)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_8KUHD)?.let {
                        sList.addAll(it)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_2K)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_QHD)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_4KDCI)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_VGA)?.let {
                        sList.addAll(it)
                    }
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_2160P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_1080P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_720P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_480P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_CIF)?.let {
                    sList.addAll(it)
                }
            }

            VideoPatternMode.TIME_LAPSE -> {
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_HIGH)?.let {
                    sList.addAll(it)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_8KUHD)?.let {
                        sList.addAll(it)
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_2K)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_QHD)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_4KDCI)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_VGA)?.let {
                        sList.addAll(it)
                    }
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_2160P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_1080P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_720P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_480P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_TIME_LAPSE_CIF)?.let {
                    sList.addAll(it)
                }
            }

            VideoPatternMode.HIGH_SPEED -> {
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_HIGH)?.let {
                    sList.addAll(it)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_4KDCI)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_VGA)?.let {
                        sList.addAll(it)
                    }
                    getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_CIF)?.let {
                        sList.addAll(it)
                    }
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_2160P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_1080P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_720P)?.let {
                    sList.addAll(it)
                }
                getCameraProfiles(cameraId, CamcorderProfile.QUALITY_HIGH_SPEED_480P)?.let {
                    sList.addAll(it)
                }
            }
        }

        if (sList.isEmpty()) {
            return null
        }

        sList.toSet().toList().let {
            sList.clear()
            sList.addAll(it)
        }

        Collections.sort(sList, object : Comparator<VideoSupportInfo> {
            override fun compare(o1: VideoSupportInfo?, o2: VideoSupportInfo?): Int {
                if (o1 == null || o2 == null) {
                    return 0
                }

                if (o1.width > o2.width) {
                    return -1
                } else if (o1.width < o2.width) {
                    return 1
                }

                if (o1.height > o2.height) {
                    return -1
                } else if (o1.height < o2.height) {
                    return 1
                }
                return 0
            }
        })

        recommendPreviewSizesMap[key] = sList.toSet().toList()
        return sList
    }

    /**
     * 获取摄像头传感器角度
     * */
    fun getSensorOrientation(cameraId: String): Int {
        val characteristics = manager?.getCameraCharacteristics(cameraId)
        return characteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
    }


    private fun checkSupportCameraId() {
        try {
            val cameraIdList = manager?.cameraIdList
            if (cameraIdList.isNullOrEmpty()) {
                faceFrontId = ""
                faceBackId = ""
                return
            }
            for (cameraId in cameraIdList) {
                val characteristics = manager?.getCameraCharacteristics(cameraId)
                val lensFacing = characteristics?.get(CameraCharacteristics.LENS_FACING)

                if (lensFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                    faceFrontId = cameraId
                } else if (lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    faceBackId = cameraId
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }

    private fun getCameraProfiles(cameraId: String, quality: Int): List<VideoSupportInfo>? {
        val list = ArrayList<VideoSupportInfo>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val profiles = CamcorderProfile.getAll(cameraId, quality)
                if (profiles != null) {
                    val audioProfile = profiles.audioProfiles.getOrNull(0)
                    profiles.videoProfiles.forEach {
                        val videoParams = VideoParamsInfo.create(
                            it.frameRate,
                            it.bitrate,
                            it.codec
                        )

                        val audioParams: AudioParamsInfo? = if (audioProfile == null) {
                            null
                        } else {
                            AudioParamsInfo.create(
                                audioProfile.channels,
                                audioProfile.sampleRate,
                                audioProfile.bitrate,
                                audioProfile.codec
                            )
                        }

                        val info = VideoSupportInfo.create(
                            it.width,
                            it.height,
                            quality,
                            videoParams,
                            audioParams
                        )
                        list.add(info)
                    }
                    return list
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w(throwable)
            }
        }

        try {
            val id = cameraId.toIntOrNull() ?: return null
            if (CamcorderProfile.hasProfile(id, quality)) {
                val profile = CamcorderProfile.get(id, quality)

                val videoParams = VideoParamsInfo.create(
                    profile.videoFrameRate,
                    profile.videoBitRate,
                    profile.videoCodec
                )

                val audioParams = AudioParamsInfo.create(
                    profile.audioChannels,
                    profile.audioSampleRate,
                    profile.audioBitRate,
                    profile.audioCodec
                )

                val info = VideoSupportInfo.create(
                    profile.videoFrameWidth,
                    profile.videoFrameHeight,
                    quality,
                    videoParams,
                    audioParams
                )
                list.add(info)
                return list
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).w(throwable)
        }
        return null
    }
}