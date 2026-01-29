package com.proxy.service.apihttp.info.common.ssl

import android.annotation.SuppressLint
import com.proxy.service.apihttp.info.common.okhttp.IOkhttpConfig
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import okhttp3.OkHttpClient
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
object TrustCerManager {

    fun parse(builder: OkHttpClient.Builder, config: IOkhttpConfig) {
        if (
            config.getServerCerAssetsName().isNullOrEmpty() &&
            config.getClientCerAssetsName().isNullOrEmpty() &&
            config.getClientCerPassWord().isNullOrEmpty() &&
            config.getX509TrustManager() == null
        ) {
            if (CoreConfig.isDebug) {
                debug(builder)
            }
            return
        }

        val x509TrustManager = ApiX509TrustManager(config.getX509TrustManager())
        getSSLSocketFactory(
            config.getServerCerAssetsName(),
            config.getClientCerAssetsName(),
            config.getClientCerPassWord(),
            x509TrustManager
        )?.let {
            builder.sslSocketFactory(
                it,
                x509TrustManager
            )
        }
    }

    private fun debug(builder: OkHttpClient.Builder) {
        val trustAllCerts = DebugX509TrustManager()
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustAllCerts), SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory

        builder.sslSocketFactory(sslSocketFactory, trustAllCerts)
    }

    private fun getSSLSocketFactory(
        serverCerAssetsName: String?,
        clientCerAssetsName: String?,
        clientCerPassWord: String?,
        x509TrustManager: X509TrustManager
    ): SSLSocketFactory? {
        val application = CsContextManager.getApplication()
        val sslContext: SSLContext = SSLContext.getInstance("TLS")

        val serverFactory: TrustManagerFactory? = if (!serverCerAssetsName.isNullOrEmpty()) {
            val factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            val certificateFactory = CertificateFactory.getInstance("X.509")
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            application.assets.open(serverCerAssetsName).use { certificate ->
                keyStore.setCertificateEntry(
                    "1",
                    certificateFactory.generateCertificate(certificate)
                )
            }
            factory.init(keyStore)
            factory
        } else {
            null
        }

        val clientFactory: KeyManagerFactory? =
            if (!clientCerAssetsName.isNullOrEmpty() && !clientCerPassWord.isNullOrEmpty()) {
                val factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
                val clientKeyStore = KeyStore.getInstance("BKS")
                application.assets.open(clientCerAssetsName).use { clientCert ->
                    clientKeyStore.load(clientCert, clientCerPassWord.toCharArray())
                }
                factory.init(clientKeyStore, clientCerPassWord.toCharArray())
                factory
            } else {
                null
            }

        val km = clientFactory?.keyManagers
        val tm = if (serverFactory == null) {
            arrayOf<TrustManager>(x509TrustManager)
        } else {
            serverFactory.trustManagers
        }

        sslContext.init(km, tm, SecureRandom())
        return sslContext.socketFactory
    }
}

@SuppressLint("CustomX509TrustManager")
private class ApiX509TrustManager(val x509TrustManager: X509TrustManager?) : X509TrustManager {

    private val defaultTrustManager: X509TrustManager = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm()
    ).apply {
        init(null as? KeyStore?)
    }.trustManagers
        .filterIsInstance<X509TrustManager>()
        .first()

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        (x509TrustManager ?: defaultTrustManager).checkClientTrusted(chain, authType)
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        (x509TrustManager ?: defaultTrustManager).checkServerTrusted(chain, authType)
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return (x509TrustManager ?: defaultTrustManager).acceptedIssuers
    }
}

@SuppressLint("CustomX509TrustManager")
private class DebugX509TrustManager : X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Nothing
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Nothing
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf()
    }

}

