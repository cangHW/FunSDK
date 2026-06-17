package com.proxy.service.webserver.info.server.factory.route.page

import android.text.TextUtils
import com.proxy.service.core.framework.io.file.read.CsFileReadUtils
import com.proxy.service.webserver.info.server.factory.base.BaseDispatch
import com.proxy.service.webserver.info.server.util.HttpJsonResponse
import fi.iki.elonen.NanoHTTPD.Response
import java.io.ByteArrayInputStream

/**
 * @author: cangHX
 * @date: 2026/6/17 09:51
 * @desc:
 */
abstract class BasePageRoute : BaseDispatch() {

    protected fun guessMimeType(path: String): String {
        return when {
            path.endsWith(".html", ignoreCase = true) -> "text/html; charset=utf-8"
            path.endsWith(".js", ignoreCase = true) -> "application/javascript; charset=utf-8"
            path.endsWith(".css", ignoreCase = true) -> "text/css; charset=utf-8"
            path.endsWith(".json", ignoreCase = true) -> "application/json; charset=utf-8"
            path.endsWith(".svg", ignoreCase = true) -> "image/svg+xml"
            path.endsWith(".png", ignoreCase = true) -> "image/png"
            else -> "application/octet-stream"
        }
    }

    protected fun serveMergedAsset(assetPath: String, mime: String): Response? {
        if (TextUtils.isEmpty(assetPath)) {
            return null
        }

        return try {
            val bytes = CsFileReadUtils.setSourceAssetPath(assetPath).readBytes()
            if (bytes.isEmpty()) {
                return null
            }
            HttpJsonResponse.stream(
                Response.Status.OK,
                mime,
                ByteArrayInputStream(bytes),
                bytes.size.toLong()
            )
        } catch (_: Throwable) {
            null
        }
    }
}
