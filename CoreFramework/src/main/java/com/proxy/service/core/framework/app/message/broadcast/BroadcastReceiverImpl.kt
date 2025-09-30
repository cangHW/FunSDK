package com.proxy.service.core.framework.app.message.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.CsExcellentSet
import com.proxy.service.core.framework.collections.type.Type
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.CountDownLatch

/**
 * @author: cangHX
 * @data: 2024/9/23 16:24
 * @desc:
 */
class BroadcastReceiverImpl : BroadcastReceiver() {

    companion object {
        const val TAG = "${CoreConfig.TAG}Msg_Broadcast"
        private const val ACTION = "com.proxy.service.core.framework.Event"

        private val handlerThread = HandlerThread("${TAG}${System.currentTimeMillis()}").apply {
            start()
        }
        private val handler = Handler(handlerThread.looper)

        private val receiverMap = CsExcellentMap<String, CsExcellentSet<BroadcastMessageListener>>()

        /**
         * 添加弱引用回调
         * */
        fun addWeakReceiverListener(action: String, listener: BroadcastMessageListener) {
            receiverMap.runInTransaction {
                var set = receiverMap.get(action)
                if (set == null) {
                    set = CsExcellentSet(Type.WEAK)
                    receiverMap.putSync(action, set)
                }
                set.putSync(listener)
            }
        }

        /**
         * 移除弱引用回调
         * */
        fun removeReceiverListener(action: String, listener: BroadcastMessageListener) {
            receiverMap.get(action)?.removeSync(listener)
        }

        /**
         * 发送无序广播
         *
         * @param action                发送意图
         * @param toPkg                 接收方包名
         * @param uri                   待发送内容，可为空
         * @param extras                待发送内容，可为空
         * */
        fun send(action: String, toPkg: String, uri: Uri?, extras: Bundle?) {
            val context = CsContextManager.getApplication()
            val intent = createIntent(action, toPkg, uri, extras)
            context.sendBroadcast(intent)
        }

        /**
         * 发送无序广播
         *
         * @param action                发送意图
         * @param toPkg                 接收方包名
         * @param uri                   待发送内容，可为空
         * @param extras                待发送内容，可为空
         * */
        fun sendOrder(action: String, toPkg: String, uri: Uri?, extras: Bundle?): Bundle? {
            var bundle: Bundle? = null

            val context = CsContextManager.getApplication()
            val intent = createIntent(action, toPkg, uri, extras)

            val latch = CountDownLatch(1)

            val finalReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    bundle = getResultExtras(true)
                    latch.countDown()
                }
            }

            context.sendOrderedBroadcast(
                intent,
                null,
                finalReceiver,
                handler,
                0,
                null,
                null
            )
            latch.await()
            return bundle
        }

        private fun createIntent(
            action: String,
            toPkg: String,
            uri: Uri?,
            extras: Bundle?
        ): Intent {
            val intent = Intent(ACTION)
            intent.addCategory(action)
            intent.setPackage(CsAppUtils.getPackageName())
            intent.setClassName(toPkg, BroadcastReceiverImpl::class.java.name)
            intent.data = uri
            extras?.let {
                intent.putExtras(it)
            }
            return intent
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            return
        }
        if (intent?.action != ACTION) {
            return
        }

        val data = intent.data
        val extras = intent.extras
        val fromPkg = intent.`package`

        CsLogger.tag(TAG).d("onReceive action=${intent.categories} extras=$extras, data=$data")

        intent.categories.forEach { category ->
            receiverMap.get(category)?.forEachSync {
                try {
                    if (it is OrderedBroadcastMessageListener) {
                        val result = it.onOrderReceive(context, fromPkg ?: "", data, extras)
                        setResultExtras(result)
                    } else {
                        it.onReceive(context, fromPkg ?: "", data, extras)
                    }

                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
            }
        }
    }
}