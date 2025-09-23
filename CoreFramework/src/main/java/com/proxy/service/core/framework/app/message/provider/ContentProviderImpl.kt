package com.proxy.service.core.framework.app.message.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.message.process.bean.ShareMessage
import com.proxy.service.core.framework.app.message.process.request.RequestDispatch
import com.proxy.service.core.framework.app.message.process.response.ResponseDispatch
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/9/15 17:57
 * @desc:
 */
class ContentProviderImpl : ContentProvider() {

    companion object {
        const val TAG = "${CoreConfig.TAG}ContentProvider"

        private const val AUTHORITY_SUFFIX = ".proxy_share_data_provider"

        /**
         * 等待初始化的最长时间
         * */
        private const val TIME_OUT_WAITING_FOR_INIT = 3 * 1000L

        private val receiverMap = CsExcellentMap<String, ProviderMessageListener>()

        /**
         * 添加回调监听
         * */
        fun addReceiverListener(method: String, listener: ProviderMessageListener) {
            receiverMap.putSync(method, listener)
        }

        /**
         * 移除回调监听
         * */
        fun removeReceiverListener(method: String) {
            receiverMap.removeSync(method)
        }

        /**
         * 发送消息
         * */
        fun sendMessage(toPkg: String, method: String, arg: String?, extras: Bundle?): Bundle?{
            return try {
                CsContextManager.getApplication().contentResolver?.call(
                    getUri(toPkg),
                    method,
                    arg,
                    extras
                )
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
                null
            }
        }

        /**
         * 获取 uri
         * */
        private fun getUri(destPkg: String): Uri {
            return Uri.parse("content://${destPkg}${AUTHORITY_SUFFIX}")
        }

    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
        val listener =
            receiverMap.getOrWait(method, TIME_OUT_WAITING_FOR_INIT, TimeUnit.MILLISECONDS)

        val fromPkg = callingPackage ?: ""
        CsLogger.tag(TAG)
            .d("receive: fromPkg = $fromPkg, method = $method, arg = $arg, extras = $extras")

        return listener?.onReceive(context, fromPkg, arg, extras)
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return -1
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return -1
    }
}