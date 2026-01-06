package com.proxy.service.core.framework.io.file.read.source

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.InputStream

/**
 * @author: cangHX
 * @data: 2026/1/6 14:48
 * @desc:
 */
class AssetsSource(private val assetPath: String) : InputStreamSource(null) {

    companion object {
        private const val TAG = "${CoreConfig.TAG}FileRead_Assets"
    }

    override fun getSourceStream(): InputStream? {
        try {
            val context = CsContextManager.getApplication()
            return context.assets.open(assetPath)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

}