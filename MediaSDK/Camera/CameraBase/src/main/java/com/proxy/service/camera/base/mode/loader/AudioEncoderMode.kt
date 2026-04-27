package com.proxy.service.camera.base.mode.loader

import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author: cangHX
 * @data: 2026/4/27 17:20
 * @desc:
 */
sealed class AudioEncoderMode private constructor(
    val encoder: Int
) {

    object DEFAULT : AudioEncoderMode(MediaRecorder.AudioEncoder.DEFAULT)

    object AMR_NB : AudioEncoderMode(MediaRecorder.AudioEncoder.AMR_NB)

    object AMR_WB : AudioEncoderMode(MediaRecorder.AudioEncoder.AMR_WB)

    object AAC : AudioEncoderMode(MediaRecorder.AudioEncoder.AAC)

    object HE_AAC : AudioEncoderMode(MediaRecorder.AudioEncoder.HE_AAC)

    object AAC_ELD : AudioEncoderMode(MediaRecorder.AudioEncoder.AAC_ELD)

    object VORBIS : AudioEncoderMode(MediaRecorder.AudioEncoder.VORBIS)

    @RequiresApi(Build.VERSION_CODES.Q)
    object OPUS : AudioEncoderMode(MediaRecorder.AudioEncoder.OPUS)

    /**
     * 自定义
     * */
    class Custom(encoder: Int) : AudioEncoderMode(encoder)

    companion object {

        fun value(encoder: Int): AudioEncoderMode {
            if (encoder == DEFAULT.encoder) {
                return DEFAULT
            }

            if (encoder == AMR_NB.encoder) {
                return AMR_NB
            }

            if (encoder == AMR_WB.encoder) {
                return AMR_WB
            }

            if (encoder == AAC.encoder) {
                return AAC
            }

            if (encoder == HE_AAC.encoder) {
                return HE_AAC
            }

            if (encoder == AAC_ELD.encoder) {
                return AAC_ELD
            }

            if (encoder == VORBIS.encoder) {
                return VORBIS
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (encoder == OPUS.encoder) {
                    return OPUS
                }
            }

            return Custom(encoder)
        }

    }
}