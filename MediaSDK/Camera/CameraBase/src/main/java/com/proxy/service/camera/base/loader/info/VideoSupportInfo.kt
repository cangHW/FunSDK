package com.proxy.service.camera.base.loader.info

/**
 * @author: cangHX
 * @data: 2026/4/21 18:00
 * @desc:
 */
class VideoSupportInfo private constructor(
    type: Int,
    ratio: Float,
    width: Int,
    height: Int,
    val quality: Int,
    val videoParams: VideoParamsInfo
) : SupportSize(
    type,
    ratio,
    width,
    height
) {

    companion object {

        fun create(
            width: Int,
            height: Int,
            quality: Int,
            videoParams: VideoParamsInfo
        ): VideoSupportInfo {
            val size = create(width, height)
            return VideoSupportInfo(
                size.type,
                size.ratio,
                size.width,
                size.height,
                quality,
                videoParams
            )
        }
    }

    override fun toString(): String {
        return "{type=${typeString()}, ratio=$ratio, width=$width, height=$height, quality=$quality, videoParams=$videoParams}"
    }

}

class VideoParamsInfo private constructor(
    val frameRate: Int,
    val bitrate: Int,
    val encoder: Int
) {
    companion object {
        fun create(frameRate: Int, bitrate: Int, encoder: Int): VideoParamsInfo {
            return VideoParamsInfo(frameRate, bitrate, encoder)
        }
    }

    override fun toString(): String {
        return "{frameRate=$frameRate, bitrate=$bitrate, encoder=$encoder}"
    }
}

class AudioParamsInfo private constructor(
    val channels: Int,
    val sampleRate: Int,
    val bitrate: Int,
    val encoder: Int
) {
    companion object {
        fun create(channels: Int, sampleRate: Int, bitrate: Int, encoder: Int): AudioParamsInfo {
            return AudioParamsInfo(channels, sampleRate, bitrate, encoder)
        }
    }
}