package com.proxy.service.core.framework.app.message.broadcast.factory.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.text.TextUtils
import androidx.annotation.Keep
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.install.CsInstallUtils
import com.proxy.service.core.framework.app.message.broadcast.callback.OrderBroadcastCallback
import com.proxy.service.core.framework.app.message.broadcast.constants.BroadcastConstants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/1/19 10:32
 * @desc:
 */
abstract class AbstractBroadcast<T>(
    protected val action: String
) : IBroadcast<T> {

    companion object {
        private val handlerThread =
            HandlerThread("StaticBroadcast-${System.currentTimeMillis()}").apply {
                start()
            }
        private val handler = Handler(handlerThread.looper)
    }

    protected val TAG = "${CoreConfig.TAG}Msg_Broadcast_Send"

    protected var toPkg: String? = null
    protected var uri: Uri? = null
    protected var extras: Bundle? = null

    override fun setToPackage(toPkg: String): T {
        this.toPkg = toPkg
        return this as T
    }

    override fun setUri(uri: Uri): T {
        this.uri = uri
        return this as T
    }

    override fun setExtras(extras: Bundle): T {
        this.extras = extras
        return this as T
    }


    override fun send() {
        if (!TextUtils.isEmpty(toPkg)) {
            if (!CsInstallUtils.isInstallApp(toPkg ?: "")) {
                CsLogger.tag(TAG).w("The target app may not be installed. package: $toPkg")
            }
        }

        CsContextManager.getApplication().sendBroadcast(createFinalIntent())
    }

    override fun sendOrder(callback: OrderBroadcastCallback) {
        if (!TextUtils.isEmpty(toPkg)) {
            if (!CsInstallUtils.isInstallApp(toPkg ?: "")) {
                CsLogger.tag(TAG).w("The target app may not be installed. package: $toPkg")
            }
        }

        val finalReceiver = object : BroadcastReceiver() {
            @Keep
            override fun onReceive(context: Context, intent: Intent) {
                val bundle = getResultExtras(true)
                if (bundle.classLoader == null) {
                    bundle.classLoader = javaClass.classLoader
                }
                bundle.getString("miracle")
                callback.onFinal(
                    resultData == BroadcastConstants.ORDER_RESPONSE,
                    bundle
                )
            }
        }

        CsContextManager.getApplication().sendOrderedBroadcast(
            createFinalIntent(),
            null,
            finalReceiver,
            handler,
            0,
            BroadcastConstants.ORDER_REQUEST,
            null
        )
    }


    protected open fun createFinalIntent(): Intent {
        val intent = createIntent()
        toPkg?.let {
            intent.setPackage(it)
        }
        intent.data = uri
        extras?.let {
            intent.putExtras(it)
        }
        intent.putExtra(BroadcastConstants.PROCESS_NAME, CsAppUtils.getProcessName())
        intent.putExtra(BroadcastConstants.ACTION_TYPE_NAME, action)
        return intent
    }

    protected abstract fun createIntent(): Intent
}