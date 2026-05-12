package com.proxy.service.core.framework.app.message.process.channel.provider

import android.content.Context
import android.os.Bundle
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.bean.ShareMessageFactory
import com.proxy.service.core.framework.app.message.process.callback.RequestCallback
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.app.message.process.request.RequestDispatch
import com.proxy.service.core.framework.app.message.process.response.ResponseDispatch
import com.proxy.service.core.framework.app.message.provider.ContentProviderImpl
import com.proxy.service.core.framework.app.message.provider.ProviderMessageListener
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2025/9/18 18:12
 * @desc:
 */
class ProviderFactory private constructor() : BaseFactory(), ProviderMessageListener {

    companion object {
        private val _instance by lazy { ProviderFactory() }

        fun getInstance(): ProviderFactory {
            return _instance
        }
    }

    /**
     * 消息是否可以发送
     * */
    override fun isCanSend(toPkg: String): Boolean {
        val result = CsInstallUtils.isInstallApp(toPkg)
        if (!result) {
            CsLogger.tag(ShareDataConstants.TAG)
                .e("Since the target application cannot be searched, provider communication cannot be used. toPkg=$toPkg")
        }
        return result
    }

    /**
     * 发送消息
     * */
    override fun send(toPkg: String, message: ShareMessage): ShareMessage? {
        if (!isCanSend(toPkg)) {
            return ShareMessageFactory.createResponseError(
                message,
                RequestCallback.ERROR_CODE_UNINSTALL.toString()
            )
        }

        CsLogger.tag(ShareDataConstants.TAG)
            .d("ProviderFactory send msg. toPkg=$toPkg, message=$message")

        val requestBundle = createBundleByShareMessage(message)
        val responseBundle = ContentProviderImpl.sendMessage(
            toPkg,
            ShareDataConstants.SHARE_DATA_PROVIDER_METHOD_NAME,
            null,
            requestBundle
        )
        val result = getShareMessageFromBundle(responseBundle)
        CsLogger.tag(ShareDataConstants.TAG)
            .d("ProviderFactory call msg. fromPkg=$toPkg, message=$result")
        return result
    }

    /**
     * 接收到消息
     */
    override fun onReceive(
        context: Context?,
        fromPkg: String,
        arg: String?,
        extras: Bundle?
    ): Bundle? {
        val message: ShareMessage = getShareMessageFromBundle(extras) ?: return null

        CsLogger.tag(ShareDataConstants.TAG)
            .d("ProviderFactory receive msg. fromPkg=$fromPkg, message=$message")

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

}