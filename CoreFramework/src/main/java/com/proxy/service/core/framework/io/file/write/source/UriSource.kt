package com.proxy.service.core.framework.io.file.write.source

import android.net.Uri
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2026/1/6 21:20
 * @desc:
 */
class UriSource(private val uri: Uri) : InputStreamSource(null) {

    companion object {
        private const val TAG = "${CoreConfig.TAG}FileWrite_Uri"
    }

    override fun getSourceStream(): InputStream? {
        try {
            val contentResolver = CsContextManager.getApplication().contentResolver
            return contentResolver.openInputStream(uri)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

}