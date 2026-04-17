package com.proxy.service.camera.info.page.cache

import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.base.loader.info.SupportSize
import com.proxy.service.camera.base.mode.loader.CameraFaceMode
import com.proxy.service.camera.info.utils.CameraUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.sp.CsSpManager
import com.proxy.service.core.framework.system.screen.CsScreenUtils
import com.proxy.service.core.framework.system.screen.info.ScreenInfo
import com.proxy.service.core.service.media.CsMediaCamera

/**
 * @author: cangHX
 * @data: 2026/4/12 15:40
 * @desc:
 */
object CameraSettingCache {

    private const val TAG = "${CameraConstants.TAG}CameraSetting"

    private const val SP_NAME = "CsCoreCamera"
    private val sp = CsSpManager.name(SP_NAME).getController()
    private var screenInfo: ScreenInfo = CsScreenUtils.getScreenRealInfo()

    private const val KEY_SCREEN_SUPPORT_SIZE_TYPE = "key_screen_support_size_type"
    private const val KEY_PICTURE_CAPTURE = "key_picture_capture_"
    private const val KEY_VIDEO_RECORD = "key_video_record_"


    fun getScreenSize(): ScreenInfo {
        return screenInfo
    }

    fun getSupportSizeType(): Int {
        var type: Int = sp.getInt(KEY_SCREEN_SUPPORT_SIZE_TYPE, -1)
        if (type == -1) {
            type = CameraUtils.getSupportSizeType(screenInfo.screenWidth, screenInfo.screenHeight)
            sp.put(KEY_SCREEN_SUPPORT_SIZE_TYPE, type)
        }
        return type
    }


    fun getPreviewSize(mode: CameraFaceMode): SupportSize {
        val sizes = CsMediaCamera.getSupportedCaptureSizes(mode)
        if (sizes.isNullOrEmpty()) {
            return SupportSize.create(2560, 1080)
        }
        var size = CameraUtils.calculateSize(sizes, screenInfo.screenWidth, screenInfo.screenHeight)
        if (size == null) {
            size = sizes[0]
        }
        return size
    }


    fun getPictureCaptureSize(mode: CameraFaceMode): SupportSize {
        try {
            val json = sp.getString(createKey(KEY_PICTURE_CAPTURE, mode), null)
            val size = SupportSize.fromJson(json)
            if (size != null) {
                return size
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return createDefaultPictureCaptureSize(mode)
    }

    fun getPictureCaptureAllSupportSize(mode: CameraFaceMode): List<SupportSize> {
        val list = ArrayList<SupportSize>()

        val type = getSupportSizeType()
        val sizes = CsMediaCamera.getSupportedCaptureSizes(mode)

        sizes?.forEach {
            if (it.type <= type) {
                list.add(it)
            }
        }

        return list
    }

    fun savaPictureCaptureSize(mode: CameraFaceMode, size: SupportSize) {
        sp.put(createKey(KEY_PICTURE_CAPTURE, mode), size.toJson())
    }


    fun getVideoRecordSize(mode: CameraFaceMode): SupportSize {
        try {
            val json = sp.getString(createKey(KEY_VIDEO_RECORD, mode), null)
            val size = SupportSize.fromJson(json)
            if (size != null) {
                return size
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return createDefaultVideoRecordSize(mode)
    }

    fun getVideoRecordAllSupportSize(mode: CameraFaceMode): List<SupportSize> {
        val list = ArrayList<SupportSize>()

        val type = getSupportSizeType()
        val sizes = CsMediaCamera.getSupportedRecordSizes(mode)

        sizes?.forEach {
            if (it.type <= type) {
                list.add(it)
            }
        }

        return list
    }

    fun savaVideoRecordSize(mode: CameraFaceMode, size: SupportSize) {
        sp.put(createKey(KEY_VIDEO_RECORD, mode), size.toJson())
    }


    private fun createDefaultPictureCaptureSize(mode: CameraFaceMode): SupportSize {
        val sizes = CsMediaCamera.getSupportedCaptureSizes(mode)
        if (sizes.isNullOrEmpty()) {
            return SupportSize.create(2560, 1080)
        }
        var size = CameraUtils.calculateSize(sizes, screenInfo.screenWidth, screenInfo.screenHeight)
        if (size == null) {
            size = sizes[0]
        }
        savaPictureCaptureSize(mode, size)
        return size
    }

    private fun createDefaultVideoRecordSize(mode: CameraFaceMode): SupportSize {
        val sizes = CsMediaCamera.getSupportedRecordSizes(mode)
        if (sizes.isNullOrEmpty()) {
            return SupportSize.create(2560, 1080)
        }
        var size = CameraUtils.calculateSize(sizes, screenInfo.screenWidth, screenInfo.screenHeight)
        if (size == null) {
            size = sizes[0]
        }
        savaVideoRecordSize(mode, size)
        return size
    }

    private fun createKey(prefix: String, mode: CameraFaceMode): String {
        return "$prefix${mode.getCameraId()}"
    }
}