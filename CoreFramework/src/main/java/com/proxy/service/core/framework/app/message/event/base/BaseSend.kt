package com.proxy.service.core.framework.app.message.event.base

import android.text.TextUtils
import com.proxy.service.core.framework.app.message.event.controller.ListDataController
import com.proxy.service.core.framework.app.message.event.controller.SingleDataController
import com.proxy.service.core.framework.system.security.md5.CsMd5Utils
import java.util.WeakHashMap
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/11/29 10:37
 * @desc:
 */
abstract class BaseSend<T : IEvent>(
    protected var callback: T?
) : ISend {

    companion object {
        private val TEMP_TAG = AtomicInteger(0)
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

    protected val tag = createTag()

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
     * 创建唯一标识
     * */
    private fun createTag(): String {
        var tag = CsMd5Utils.getMD5(this.hashCode().toString() + System.currentTimeMillis())

        if (TextUtils.isEmpty(tag)) {
            tag = TEMP_TAG.getAndIncrement().toString()
        }
        return tag
    }

    /**
     * 检查状态看是否需要发送
     * */
    protected abstract fun checkState()

}