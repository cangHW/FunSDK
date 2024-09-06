package com.proxy.service.webview.base.web.permissions

/**
 * @author: cangHX
 * @data: 2024/8/5 10:47
 * @desc:
 */
interface PermissionRequest {

    companion object {
        /**
         * 处理音频捕获（如麦克风）权限
         * */
        const val RESOURCE_AUDIO_CAPTURE: String = "android.webkit.resource.AUDIO_CAPTURE"

        /**
         * 处理MIDI Sysex权限
         * */
        const val RESOURCE_MIDI_SYSEX: String = "android.webkit.resource.MIDI_SYSEX"

        /**
         * 处理受保护媒体ID权限
         * */
        const val RESOURCE_PROTECTED_MEDIA_ID: String = "android.webkit.resource.PROTECTED_MEDIA_ID"

        /**
         * 处理视频捕获（如摄像头）权限
         * */
        const val RESOURCE_VIDEO_CAPTURE: String = "android.webkit.resource.VIDEO_CAPTURE"
    }

    /**
     * 获取权限请求的来源URI。这通常用来标识哪个网页或应用发起了权限请求
     * */
    fun getOrigin(): String

    /**
     * 获取请求的资源权限类型，例如：[PermissionRequest.RESOURCE_VIDEO_CAPTURE] 等
     * */
    fun getResources(): Array<String>

    /**
     * 授予指定资源的权限
     * */
    fun grant(resources: Array<String>)

    /**
     * 拒绝所有请求的权限
     * */
    fun deny()

}