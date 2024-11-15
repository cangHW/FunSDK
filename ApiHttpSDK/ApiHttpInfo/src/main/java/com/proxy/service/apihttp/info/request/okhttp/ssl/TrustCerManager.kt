package com.proxy.service.apihttp.info.request.okhttp.ssl

import android.annotation.SuppressLint
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * @author: cangHX
 * @data: 2024/5/21 21:03
 * @desc:
 */
@SuppressLint("CustomX509TrustManager")
class TrustCerManager : X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }

    companion object {
        fun getSSLSocketFactory(
            serverCerAssetsName: String?,
            clientCerAssetsName: String?,
            clientCerPassWord: String?
        ): SSLSocketFactory? {
            var sslContext: SSLContext? = null
            try {
                sslContext = SSLContext.getInstance("TLS")

                var serverFactory: TrustManagerFactory? = null
                if (!serverCerAssetsName.isNullOrEmpty()) {
                    val certificateFactory = CertificateFactory.getInstance("X.509")
                    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                    keyStore.load(null)
                    val certificate = CsContextManager.getApplication().assets.open(serverCerAssetsName)
                    keyStore.setCertificateEntry(
                        "1",
                        certificateFactory.generateCertificate(certificate)
                    )
                    try {
                        certificate.close()
                    } catch (throwable: Throwable) {
                        CsLogger.e(throwable)
                    }
                    serverFactory =
                        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                    serverFactory.init(keyStore)
                }

                var clientFactory: KeyManagerFactory? = null
                if (!clientCerAssetsName.isNullOrEmpty() && !clientCerPassWord.isNullOrEmpty()) {
                    val clientKeyStore = KeyStore.getInstance("BKS")
                    clientKeyStore.load(
                        CsContextManager.getApplication().assets.open(clientCerAssetsName),
                        clientCerPassWord.toCharArray()
                    )
                    clientFactory =
                        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                    clientFactory.init(clientKeyStore, clientCerPassWord.toCharArray())
                }

                if (serverFactory != null && clientFactory != null) {
                    sslContext.init(
                        clientFactory.keyManagers,
                        serverFactory.trustManagers,
                        SecureRandom()
                    )
                } else if (serverFactory != null) {
                    sslContext.init(null, serverFactory.trustManagers, SecureRandom())
                } else {
                    sslContext.init(null, arrayOf<TrustManager>(TrustCerManager()), SecureRandom())
                }
            } catch (throwable: Throwable) {
                CsLogger.e(throwable)
            }
            return sslContext?.socketFactory
        }
    }

}