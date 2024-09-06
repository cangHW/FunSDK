package com.proxy.service.webview.info.web.permissions

import com.proxy.service.webview.base.web.permissions.PermissionRequest

/**
 * @author: cangHX
 * @data: 2024/8/5 11:46
 * @desc:
 */
class PermissionRequestImpl(private val request:android.webkit.PermissionRequest?):
    PermissionRequest {
    override fun getOrigin(): String {
        return request?.origin?.toString()?:""
    }

    override fun getResources(): Array<String> {
        return request?.resources?: arrayOf()
    }

    override fun grant(resources: Array<String>) {
        request?.grant(resources)
    }

    override fun deny() {
        request?.deny()
    }
}