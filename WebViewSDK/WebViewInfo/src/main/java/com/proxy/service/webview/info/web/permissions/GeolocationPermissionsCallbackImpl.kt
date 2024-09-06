package com.proxy.service.webview.info.web.permissions

import android.webkit.GeolocationPermissions
import com.proxy.service.webview.base.web.permissions.GeolocationPermissionsCallback

/**
 * @author: cangHX
 * @data: 2024/8/5 11:44
 * @desc:
 */
class GeolocationPermissionsCallbackImpl(private val callback: GeolocationPermissions.Callback?) :
    GeolocationPermissionsCallback {
    override fun invoke(origin: String, allow: Boolean, retain: Boolean) {
        callback?.invoke(origin, allow, retain)
    }
}