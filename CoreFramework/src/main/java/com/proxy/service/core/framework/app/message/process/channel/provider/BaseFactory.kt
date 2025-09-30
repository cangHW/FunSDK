package com.proxy.service.core.framework.app.message.process.channel.provider

import android.os.Build
import android.os.Bundle
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.constants.ShareDataConstants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2025/9/29 15:51
 * @desc:
 */
abstract class BaseFactory {

    /**
     * 消息是否可以发送
     * */
    abstract fun isCanSend(toPkg: String): Boolean

    /**
     * 发送消息
     * */
    abstract fun send(toPkg: String, message: ShareMessage): ShareMessage?

    /**
     * 通过 ShareMessage 创建用于数据传输的 Bundle
     * */
    protected fun createBundleByShareMessage(message: ShareMessage): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(ShareDataConstants.KEY_BUNDLE, message)
        return bundle
    }

    /**
     * 获取 Bundle 中可能存在的 ShareMessage 数据
     * */
    protected fun getShareMessageFromBundle(bundle: Bundle?): ShareMessage? {
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
            CsLogger.tag(ShareDataConstants.TAG).e(throwable)
            null
        }
    }
}