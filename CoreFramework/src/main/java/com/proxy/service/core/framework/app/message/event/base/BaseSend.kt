package com.proxy.service.core.framework.app.message.event.base

import com.proxy.service.core.framework.app.message.event.controller.ListDataController
import com.proxy.service.core.framework.app.message.event.controller.SingleDataController
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/11/29 10:37
 * @desc:
 */
abstract class BaseSend<T : IEvent>(
    protected var callback: T?
) : ISend {

    companion object {
        private val ANY = Any()
    }

    private val temp = WeakHashMap<Class<*>, Any>()

    private val supportTypes: Set<Class<*>> = callback?.getSupportTypes() ?: HashSet()
    protected val controller: IController? = callback?.let {
        if (it.shouldReceiveOnlyLatestMessage()) {
            SingleDataController()
        } else {
            ListDataController()
        }
    }

    override fun sendEventValue(any: Any) {
        if (temp.containsKey(any.javaClass)) {
            checkSend(any)
            return
        }
        val iterator = supportTypes.iterator()
        while (iterator.hasNext()) {
            val tClass = iterator.next()
            if (tClass.isAssignableFrom(any.javaClass)) {
                temp[any.javaClass] = ANY
                checkSend(any)
                return
            }
        }
    }

    private fun checkSend(any: Any) {
        controller?.addCache(any)
        checkState()
    }

    /**
     * 检查状态看是否需要发送
     * */
    protected abstract fun checkState()

}