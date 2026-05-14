package com.proxy.service.core.framework.system.net.controller

import android.os.Looper
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.system.net.NetType
import com.proxy.service.core.framework.system.net.callback.NetConnectChangedListener
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.core.utils.ThreadUtils
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @date: 2024/7/23 11:26
 * @desc:
 */
abstract class AbstractController constructor(
    private val weakNetMapper: WeakHashMap<NetConnectChangedListener, Any>,
    private val callback: () -> Unit
) {

    companion object {
        const val TAG = "${CoreConfig.TAG}Net"
    }

    abstract fun start()

    abstract fun stop()


    private fun getListenersSafe(): List<NetConnectChangedListener> {
        return synchronized(weakNetMapper) {
            weakNetMapper.keys.toList()
        }
    }

    protected fun callNetConnected() {
        getListenersSafe().forEach {
            ThreadUtils.runUiThread {
                it.onNetConnected()
            }
        }
        callback()
    }

    protected fun callNetDisConnected() {
        getListenersSafe().forEach {
            ThreadUtils.runUiThread {
                it.onNetDisConnected()
            }
        }
        callback()
    }

    protected fun callNetChanged(type: NetType) {
        getListenersSafe().forEach {
            ThreadUtils.runUiThread {
                it.onNetChanged(type)
            }
        }
        callback()
    }
}