package com.proxy.service.core

import android.app.Application
import android.util.SparseArray
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.application.CoreApplication
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 整体架构初始化入口
 *
 * @author: cangHX
 * @data: 2024/4/28 18:14
 * @desc:
 */
object CsCore {

    private const val TAG = "${Constants.TAG}Init"
    private val isInit = AtomicBoolean(false)

    /**
     * 核心库初始化
     * */
    fun init(application: Application, isDebug: Boolean) {
        if (!isInit.compareAndSet(false, true)) {
            CsLogger.tag(TAG).d("init is ready.")
            return
        }

        var startInitTime: Long = 0
        if (isDebug) {
            startInitTime = System.currentTimeMillis()
        }

        CloudSystem.init(application, isDebug)
        CsLogger.tag(TAG).d("start init.")

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
        for (index in 0 until sparse.size()) {
            val priority = sparse.keyAt(index)
            CsLogger.tag(TAG).d("run priority = $priority")
            sparse.get(priority).forEach {
                var time: Long = 0
                if (isDebug) {
                    time = System.currentTimeMillis()
                }
                try {
                    it.onCreate(application, isDebug)
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
                if (isDebug) {
                    CsLogger.tag(TAG)
                        .d("${it.javaClass.simpleName} onCreate 耗时 ${System.currentTimeMillis() - time} 毫秒")
                }
            }
        }

        if (isDebug) {
            CsLogger.tag(TAG)
                .d("CsCore 初始化完成. 总耗时 ${System.currentTimeMillis() - startInitTime} 毫秒")
        }
    }

}