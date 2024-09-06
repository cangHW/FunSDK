package com.proxy.service.core

import android.app.Application
import android.util.SparseArray
import androidx.core.util.forEach
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.application.CoreApplication
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2024/4/28 18:14
 * @desc:
 */
object CsCore {

    private val isInit = AtomicBoolean(false)

    /**
     * 核心库初始化
     * */
    fun init(application: Application, isDebug: Boolean) {
        if (!isInit.compareAndSet(false, true)) {
            return
        }

        CloudSystem.init(application, isDebug)

        var coreApplication: CoreApplication? = null
        val sparse = SparseArray<ArrayList<CsBaseApplication>>()

        CloudSystem.getServices(CsBaseApplication::class.java).forEach {
            if (it is CoreApplication) {
                coreApplication = it
            } else {
                var list = sparse[it.priority()]
                if (list == null) {
                    list = ArrayList()
                    sparse[it.priority()] = list
                }
                list.add(it)
            }
        }
        coreApplication?.onCreate(application, isDebug)
        sparse.forEach { key, value ->
            CsLogger.d("run priority = $key")
            value.forEach {
                var time: Long = 0
                if (isDebug) {
                    time = System.currentTimeMillis()
                }
                try {
                    it.onCreate(application, isDebug)
                } catch (throwable: Throwable) {
                    CsLogger.e(throwable)
                }
                if (isDebug) {
                    CsLogger.d("${it.javaClass.simpleName} onCreate 耗时 ${System.currentTimeMillis() - time} 毫秒")
                }
            }
        }
    }

}