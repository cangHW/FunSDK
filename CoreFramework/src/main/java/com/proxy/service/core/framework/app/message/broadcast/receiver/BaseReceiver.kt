package com.proxy.service.core.framework.app.message.broadcast.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.proxy.service.core.framework.app.message.broadcast.callback.OrderedBroadcastMsgListener
import com.proxy.service.core.framework.app.message.broadcast.constants.BroadcastConstants
import com.proxy.service.core.framework.app.message.broadcast.controller.CallbackController
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/1/19 16:20
 * @desc:
 */
abstract class BaseReceiver : BroadcastReceiver() {

    /**
     * 获取日志 tag
     * */
    protected abstract fun getLogTag(): String

    /**
     * 获取消息事件
     * */
    protected abstract fun getAction(): String


    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            return
        }
        if (intent?.action != getAction()) {
            return
        }

        val action = intent.getStringExtra(BroadcastConstants.ACTION_TYPE_NAME)
        intent.removeExtra(BroadcastConstants.ACTION_TYPE_NAME)
        if (TextUtils.isEmpty(action)) {
            return
        }

        val data = intent.data
        val fromPkg = intent.`package`
        val processName = intent.getStringExtra(BroadcastConstants.PROCESS_NAME)
        intent.removeExtra(BroadcastConstants.PROCESS_NAME)
        val extras = intent.extras

        CsLogger.tag(getLogTag())
            .d("onReceive action=$action, fromPkg=$fromPkg, processName=$processName, extras=$extras, data=$data")

        var result: Bundle? = getResultExtras(false)
        CallbackController.getReceiverList(action ?: "")?.forEachSync {
            CsLogger.tag(getLogTag()).d("receiver dispatch listener=$it")

            try {
                if (it is OrderedBroadcastMsgListener) {
                    result = it.onOrderReceive(
                        context,
                        fromPkg ?: "",
                        processName ?: "",
                        data,
                        extras,
                        result
                    )
                    resultData = BroadcastConstants.ORDER_RESPONSE
                    setResultExtras(result)
                } else {
                    it.onReceive(context, fromPkg ?: "", processName ?: "", data, extras)
                }
            } catch (throwable: Throwable) {
                CsLogger.tag(getLogTag()).e(throwable)
            }
        }
    }
}