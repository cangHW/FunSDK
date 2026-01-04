package com.proxy.service.webview.base.web.chooser

import android.content.Intent
import android.net.Uri
import android.webkit.WebChromeClient

/**
 * @author: cangHX
 * @data: 2026/1/4 10:10
 * @desc:
 */
interface FileChooserParams {

    companion object {
        const val MODE_OPEN = WebChromeClient.FileChooserParams.MODE_OPEN
        const val MODE_OPEN_MULTIPLE = WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE
        const val MODE_SAVE = WebChromeClient.FileChooserParams.MODE_SAVE

        /**
         * 解析文件选择器活动返回的结果, 该方法需要与方法 [createIntent] 配合使用
         * */
        fun parseResult(resultCode: Int, data: Intent): Array<Uri>? {
            return WebChromeClient.FileChooserParams.parseResult(resultCode, data)
        }
    }

    /**
     * 返回文件选择器模式。
     * */
    fun getMode(): Int

    /**
     * 返回可接受的MIME类型数组
     * */
    fun getAcceptTypes(): Array<String>

    /**
     * 是否使用实时捕获, 例如: Camera, Microphone 等.
     * 配合方法 [getAcceptTypes] 来确定合适的捕获设备
     * */
    fun isCaptureEnabled(): Boolean

    /**
     * 返回文件选择器的标题, 如果返回 null 需要使用一个默认标题
     * */
    fun getTitle(): CharSequence?

    /**
     * 返回默认选择的文件名, 如果返回 null 则没有默认选择
     * */
    fun getFilenameHint(): String?

    /**
     * 创建一个意图来启动一个文件选择器. 配合方法 [parseResult] 来解析结果
     * 尽量使用自定义的文件选择器, 系统默认的选择器并不一定全部支持.
     * */
    fun createIntent(): Intent
}