package com.proxy.service.core.framework.app.install.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.install.status.InstallStatusEnum
import com.proxy.service.core.framework.app.install.callback.InstallReceiverListener
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.net.controller.IController
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/10/29 18:37
 * @desc:
 */
class BroadcastReceiverImpl : BroadcastReceiver() {

    companion object {
        private const val TAG = "${Constants.TAG}InstallReceiver"

        private val isStart = AtomicBoolean(false)
        private val receiver = BroadcastReceiverImpl()

        private val any = Any()
        private val weakReceiverHashMap = WeakHashMap<InstallReceiverListener, Any>()

        /**
         * 添加弱引用回调
         * */
        fun addWeakReceiverListener(listener: InstallReceiverListener) {
            synchronized(weakReceiverHashMap) {
                weakReceiverHashMap[listener] = any
                if (weakReceiverHashMap.size > 0) {
                    if (isStart.compareAndSet(false, true)) {
                        receiver.start()
                    }
                }
            }
        }

        /**
         * 移除弱引用回调
         * */
        fun removeReceiverListener(listener: InstallReceiverListener) {
            synchronized(weakReceiverHashMap) {
                weakReceiverHashMap.remove(listener)
                if (weakReceiverHashMap.size <= 0) {
                    receiver.stop()
                    isStart.set(false)
                }
            }
        }
    }

    fun start() {
        CsLogger.tag(IController.TAG).i("start")

        val intentFilter = IntentFilter()
        intentFilter.addAction(InstallStatusEnum.PACKAGE_ADDED.value)
        intentFilter.addAction(InstallStatusEnum.PACKAGE_REMOVED.value)
        intentFilter.addDataScheme("package")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CsContextManager.getApplication()
                .registerReceiver(receiver, intentFilter, Context.RECEIVER_EXPORTED)
        } else {
            CsContextManager.getApplication().registerReceiver(receiver, intentFilter)
        }
    }

    fun stop() {
        CsContextManager.getApplication().unregisterReceiver(receiver)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (weakReceiverHashMap.size <= 0) {
            synchronized(weakReceiverHashMap) {
                if (weakReceiverHashMap.size <= 0) {
                    stop()
                    return
                }
            }
        }

        if (context == null) {
            return
        }
        val statusEnum = InstallStatusEnum.of(intent?.action) ?: return
        CsLogger.tag(TAG).d("onReceive action = ${statusEnum.value}, intent = $intent")
        val packageName = intent?.data?.schemeSpecificPart ?: ""

        try {
            weakReceiverHashMap.iterator().forEach {
                try {
                    it.key.onReceive(context, statusEnum, packageName)
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }
}