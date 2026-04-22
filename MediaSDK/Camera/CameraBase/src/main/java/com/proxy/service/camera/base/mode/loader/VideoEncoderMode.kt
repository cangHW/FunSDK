package com.proxy.service.camera.base.mode.loader

import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author: cangHX
 * @data: 2026/4/22 09:51
 * @desc:
 */
sealed class VideoEncoderMode private constructor(
    val encoder: Int
) {

    object DEFAULT : VideoEncoderMode(MediaRecorder.VideoEncoder.DEFAULT)

    object H263 : VideoEncoderMode(MediaRecorder.VideoEncoder.H263)

    object H264 : VideoEncoderMode(MediaRecorder.VideoEncoder.H264)

    object MPEG_4_SP : VideoEncoderMode(MediaRecorder.VideoEncoder.MPEG_4_SP)

    object VP8 : VideoEncoderMode(MediaRecorder.VideoEncoder.VP8)

    @RequiresApi(Build.VERSION_CODES.N)
    object HEVC : VideoEncoderMode(MediaRecorder.VideoEncoder.HEVC)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object VP9 : VideoEncoderMode(MediaRecorder.VideoEncoder.VP9)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object DOLBY_VISION : VideoEncoderMode(MediaRecorder.VideoEncoder.DOLBY_VISION)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object AV1 : VideoEncoderMode(MediaRecorder.VideoEncoder.AV1)

    /**
     * 自定义
     * */
    class Custom(encoder: Int) : VideoEncoderMode(encoder)


    companion object {

        fun value(encoder: Int): VideoEncoderMode {
            if (encoder == DEFAULT.encoder) {
                return DEFAULT
            }

            if (encoder == H263.encoder) {
                return H263
            }

            if (encoder == H264.encoder) {
                return H264
            }

            if (encoder == MPEG_4_SP.encoder) {
                return MPEG_4_SP
            }

            if (encoder == VP8.encoder) {
                return VP8
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (encoder == HEVC.encoder) {
                    return HEVC
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (encoder == VP9.encoder) {
                    return VP9
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (encoder == DOLBY_VISION.encoder) {
                    return DOLBY_VISION
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (encoder == AV1.encoder) {
                    return AV1
                }
            }

            return Custom(encoder)
        }

    }
}