package com.proxy.service.core.framework.io.file.media.config

/**
 * @author: cangHX
 * @data: 2025/1/2 14:39
 * @desc:
 */
class MimeType private constructor(val type: String) {

    companion object {

        /**
         * JPEG 图像
         * */
        val IMAGE_JPEG by lazy {
            MimeType("image/jpeg")
        }

        /**
         * PNG 图像
         * */
        val IMAGE_PNG by lazy {
            MimeType("image/png")
        }

        /**
         * GIF 图像
         * */
        val IMAGE_GIF by lazy {
            MimeType("image/gif")
        }

        /**
         * BMP 图像
         * */
        val IMAGE_BMP by lazy {
            MimeType("image/bmp")
        }

        /**
         * WebP 图像
         * */
        val IMAGE_WEBP by lazy {
            MimeType("image/webp")
        }

        /**
         * MP3 音频
         * */
        val AUDIO_MPEG by lazy {
            MimeType("audio/mpeg")
        }

        /**
         * WAV 音频
         * */
        val AUDIO_WAV by lazy {
            MimeType("audio/wav")
        }

        /**
         * OGG 音频
         * */
        val AUDIO_OGG by lazy {
            MimeType("audio/ogg")
        }

        /**
         * AAC 音频
         * */
        val AUDIO_AAC by lazy {
            MimeType("audio/aac")
        }

        /**
         * FLAC 音频
         * */
        val AUDIO_FLAC by lazy {
            MimeType("audio/flac")
        }

        /**
         * MP4 视频
         * */
        val VIDEO_MP4 by lazy {
            MimeType("video/mp4")
        }

        /**
         * AVI 视频
         * */
        val VIDEO_X_MS_VIDEO by lazy {
            MimeType("video/x-msvideo")
        }

        /**
         * QuickTime 视频
         * */
        val VIDEO_QUICK_TIME by lazy {
            MimeType("video/quicktime")
        }

        /**
         * Matroska 视频（MKV）
         * */
        val VIDEO_X_MATROSKA by lazy {
            MimeType("video/x-matroska")
        }

        /**
         * WebM 视频
         * */
        val VIDEO_WEBM by lazy {
            MimeType("video/webm")
        }

        /**
         * 创建一个新格式
         * */
        fun create(type: String): MimeType {
            return MimeType(type)
        }
    }

}