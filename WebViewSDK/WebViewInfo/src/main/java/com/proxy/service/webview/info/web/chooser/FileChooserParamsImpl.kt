package com.proxy.service.webview.info.web.chooser

import android.content.Intent
import android.webkit.WebChromeClient
import com.proxy.service.webview.base.web.chooser.FileChooserParams

/**
 * @author: cangHX
 * @data: 2026/1/4 10:28
 * @desc:
 */
class FileChooserParamsImpl(
    private val fileChooser: WebChromeClient.FileChooserParams?
) : FileChooserParams {

    override fun getMode(): Int {
        return fileChooser?.mode ?: 0
    }

    override fun getAcceptTypes(): Array<String> {
        return fileChooser?.acceptTypes ?: arrayOf()
    }

    override fun isCaptureEnabled(): Boolean {
        return fileChooser?.isCaptureEnabled ?: false
    }

    override fun getTitle(): CharSequence? {
        return fileChooser?.title
    }

    override fun getFilenameHint(): String? {
        return fileChooser?.filenameHint
    }

    override fun createIntent(): Intent {
        return fileChooser?.createIntent() ?: Intent()
    }
}