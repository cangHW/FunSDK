package com.proxy.service.core.framework.app.message.process.channel.provider

import android.content.Context
import android.os.Build
import android.os.Bundle
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.request.RequestDispatch
import com.proxy.service.core.framework.app.message.process.response.ResponseDispatch
import com.proxy.service.core.framework.app.message.provider.ContentProviderImpl
import com.proxy.service.core.framework.app.message.provider.ProviderMessageListener

/**
 * @author: cangHX
 * @data: 2025/9/18 18:12
 * @desc:
 */
class ProviderFactory : ProviderMessageListener {

    companion object {
        private val _instance by lazy { ProviderFactory() }

        fun getInstance(): ProviderFactory {
            return _instance
        }
    }

    fun send(toPkg: String, message: ShareMessage): ShareMessage? {
        val requestBundle = createBundleByShareMessage(message)
        val responseBundle = ContentProviderImpl.sendMessage(
            toPkg,
            ShareDataConstants.SHARE_DATA_PROVIDER_METHOD_NAME,
            null,
            requestBundle
        )
        return getShareMessageFromBundle(responseBundle)
    }

    override fun onReceive(
        context: Context?,
        fromPkg: String,
        arg: String?,
        extras: Bundle?
    ): Bundle? {
        val message: ShareMessage = getShareMessageFromBundle(extras) ?: return null
        val resultMessage = if (message.isRequest()) {
            RequestDispatch.dispatch(fromPkg, message)
        } else if (message.isResponse()) {
            ResponseDispatch.dispatch(message)
        } else {
            null
        }
        if (resultMessage != null) {
            return createBundleByShareMessage(resultMessage)
        }
        return null
    }

    private fun createBundleByShareMessage(message: ShareMessage): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ShareDataConstants.KEY_BUNDLE, message)
        return bundle
    }

    private fun getShareMessageFromBundle(bundle: Bundle?): ShareMessage? {
        if (bundle == null) {
            return null
        }
        if (bundle.classLoader == null) {
            bundle.classLoader = javaClass.classLoader
        }
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(ShareDataConstants.KEY_BUNDLE, ShareMessage::class.java)
            } else {
                bundle.getParcelable(ShareDataConstants.KEY_BUNDLE)
            }
        } catch (throwable: Throwable) {
            null
        }
    }

}