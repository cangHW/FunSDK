package com.proxy.service.core.framework.app.message.event

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.message.event.base.IEvent
import com.proxy.service.core.framework.app.message.event.base.ISend
import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.MainThreadAlwaysSendImpl
import com.proxy.service.core.framework.app.message.event.impl.MainThreadLifecycleSendImpl
import com.proxy.service.core.framework.app.message.event.impl.WorkThreadAlwaysSendImpl
import com.proxy.service.core.framework.app.message.event.impl.WorkThreadLifecycleSendImpl
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/11/28 20:22
 * @desc:
 */
object CsEventManager {

    private val weakCallbackMap = WeakHashMap<IEvent, ISend>()

    private val handler = CsTask.launchTaskGroup("${CoreConfig.TAG}Event")

    /**
     * 添加在主线程进行回调的监听, 弱引用
     *
     * @param callback          回调接口
     * @param lifecycleOwner    生命周期提供者, 作用：1、用于自动移除回调；2、在对应页面可见时再接收消息
     * */
    fun addWeakCallback(callback: MainThreadEventCallback, lifecycleOwner: LifecycleOwner? = null) {
        if (lifecycleOwner == null) {
            weakCallbackMap[callback] = MainThreadAlwaysSendImpl(callback)
        } else {
            val send = MainThreadLifecycleSendImpl(callback, lifecycleOwner)
            send.initLifecycle()
            weakCallbackMap[callback] = send
        }
    }

    /**
     * 添加在子线程进行回调的监听, 弱引用
     *
     * @param callback          回调接口
     * @param lifecycleOwner    生命周期提供者, 作用：1、用于自动移除回调；2、在对应页面可见时再接收消息
     * */
    fun addWeakCallback(callback: WorkThreadEventCallback, lifecycleOwner: LifecycleOwner? = null) {
        if (lifecycleOwner == null) {
            weakCallbackMap[callback] = WorkThreadAlwaysSendImpl(callback)
        } else {
            val send = WorkThreadLifecycleSendImpl(callback, lifecycleOwner)
            send.initLifecycle()
            weakCallbackMap[callback] = send
        }
    }

    /**
     * 移除监听
     * */
    fun remove(callback: MainThreadEventCallback) {
        weakCallbackMap.remove(callback)
    }

    /**
     * 移除监听
     * */
    fun remove(callback: WorkThreadEventCallback) {
        weakCallbackMap.remove(callback)
    }

    /**
     * 根据数据类型发送给对应监听
     * */
    fun sendEventValue(any: Any) {
        handler?.start {
            val iterator = weakCallbackMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                entry.key ?: continue
                val value = entry.value ?: continue
                try {
                    value.sendEventValue(any)
                } catch (throwable: Throwable) {
                    CsLogger.tag(EventConfig.TAG).e(throwable)
                }
            }
        }
    }

}