package com.proxy.service.core.framework.app.message.event

import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.message.event.base.IEvent
import com.proxy.service.core.framework.app.message.event.base.ISend
import com.proxy.service.core.framework.app.message.event.callback.MainThreadEventCallback
import com.proxy.service.core.framework.app.message.event.callback.WorkThreadEventCallback
import com.proxy.service.core.framework.app.message.event.config.EventConfig
import com.proxy.service.core.framework.app.message.event.impl.MainThreadAlwaysSendImpl
import com.proxy.service.core.framework.app.message.event.impl.MainThreadLifecycleSendImpl
import com.proxy.service.core.framework.app.message.event.impl.WorkThreadAlwaysSendImpl
import com.proxy.service.core.framework.app.message.event.impl.WorkThreadLifecycleSendImpl
import com.proxy.service.core.framework.collections.CsExcellentMap
import com.proxy.service.core.framework.collections.type.Type
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask

/**
 * 应用内 event 消息相关工具
 *
 * @author: cangHX
 * @data: 2024/11/28 20:22
 * @desc:
 */
object CsEventManager {

    private val callbackMap = CsExcellentMap<IEvent, ISend>(Type.WEAK)
    private val handler = CsTask.launchTaskGroup(EventConfig.THREAD_EVENT)

    /**
     * 添加在主线程进行回调的监听, 弱引用
     *
     * @param callback          回调接口
     * @param lifecycleOwner    生命周期提供者, 作用：1、用于自动移除回调；2、在对应页面可见时再接收消息
     * */
    fun addWeakCallback(callback: MainThreadEventCallback, lifecycleOwner: LifecycleOwner? = null) {
        if (lifecycleOwner == null) {
            callbackMap.putSync(callback, MainThreadAlwaysSendImpl(callback))
        } else {
            val send = MainThreadLifecycleSendImpl(callback, lifecycleOwner)
            send.initLifecycle()
            callbackMap.putSync(callback, send)
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
            callbackMap.putSync(callback, WorkThreadAlwaysSendImpl(callback))
        } else {
            val send = WorkThreadLifecycleSendImpl(callback, lifecycleOwner)
            send.initLifecycle()
            callbackMap.putSync(callback, send)
        }
    }

    /**
     * 移除监听
     * */
    fun removeCallback(callback: IEvent) {
        callbackMap.removeSync(callback)
    }

    /**
     * 根据数据类型发送给对应监听
     * */
    fun sendEventValue(any: Any) {
        handler?.start {
            callbackMap.forEachSync { _, iSend ->
                try {
                    iSend.sendEventValue(any)
                } catch (throwable: Throwable) {
                    CsLogger.tag(EventConfig.TAG).e(throwable)
                }
            }
        }
    }

}