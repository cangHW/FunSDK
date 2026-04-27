package com.proxy.service.camera.base.constants

import com.proxy.service.camera.base.mode.loader.AudioEncoderMode
import com.proxy.service.camera.base.mode.loader.CameraAfMode
import com.proxy.service.camera.base.mode.loader.VideoEncoderMode
import com.proxy.service.camera.base.mode.view.CameraViewAfMode
import com.proxy.service.camera.base.mode.view.CameraViewMode

/**
 * @author: cangHX
 * @data: 2026/2/4 16:07
 * @desc:
 */
object CameraConstants {

    const val TAG = "Media_Camera_"

    val DEFAULT_VIEW_MODE: CameraViewMode = CameraViewMode.SURFACE_VIEW

    val DEFAULT_CAMERA_AF_MODE: CameraAfMode = CameraAfMode.AfAutoMode
    val DEFAULT_CAMERA_VIEW_AF_MODE: CameraViewAfMode = CameraViewAfMode.AfTouchMode()


    const val DEFAULT_PICTURE_CAPTURE_SIZE_WIDTH = 2560
    const val DEFAULT_PICTURE_CAPTURE_SIZE_HEIGHT = 1080

    const val DEFAULT_VIDEO_RECORD_SIZE_WIDTH = 1920
    const val DEFAULT_VIDEO_RECORD_SIZE_HEIGHT = 1080

    val DEFAULT_VIDEO_ENCODER_MODE: VideoEncoderMode = VideoEncoderMode.H264
    const val DEFAULT_VIDEO_FRAME_RATE: Int = 30
    const val DEFAULT_VIDEO_ENCODING_BIT_RATE: Int = 20000000

    const val DEFAULT_AUDIO_ENABLED: Boolean = true
    val DEFAULT_AUDIO_ENCODER_MODE: AudioEncoderMode = AudioEncoderMode.AAC
    const val DEFAULT_AUDIO_SAMPLING_RATE: Int = 44100
    const val DEFAULT_AUDIO_ENCODING_BIT_RATE: Int = 128000
    const val DEFAULT_AUDIO_CHANNELS: Int = 1

}