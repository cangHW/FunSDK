package com.proxy.service.webview.info.web.error

import android.net.http.SslCertificate
import com.proxy.service.webview.base.web.error.SslError

/**
 * @author: cangHX
 * @data: 2024/8/3 10:52
 * @desc:
 */
class SslErrorImpl(private val sslError: android.net.http.SslError) :
    SslError {
    override fun getCertificate(): SslCertificate {
        return sslError.certificate
    }

    override fun addError(error: Int): Boolean {
        return sslError.addError(error)
    }

    override fun hasError(error: Int): Boolean {
        return sslError.hasError(error)
    }

    override fun getPrimaryError(): Int {
        return sslError.primaryError
    }

    override fun getUrl(): String {
        return sslError.url
    }
}