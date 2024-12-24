package com.proxy.service.core

import android.app.Application
import android.util.SparseArray
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.application.base.BaseCoreFw
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.constants.CoreConfig
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

    private const val TAG = "${CoreConfig.TAG}Init"
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
        CoreConfig.isDebug = isDebug

        val sparseApplication = SparseArray<ArrayList<CsBaseApplication>>()
        val sparseConfig = SparseArray<ArrayList<CsBaseConfig>>()

        CloudSystem.getServices(BaseCoreFw::class.java).forEach {
            when (it) {
                is CsBaseApplication -> {
                    addToArray(sparseApplication, it)
                }

                is CsBaseConfig -> {
                    addToArray(sparseConfig, it)
                }

                else -> {
                    // do nothing
                }
            }
        }

        initConfig(sparseConfig, application, isDebug)

        CsLogger.tag(TAG).d("start init application.")

        initApplication(sparseApplication, application, isDebug)

        if (isDebug) {
            CsLogger.tag(TAG)
                .d("CsCore 初始化完成. 总耗时 ${System.currentTimeMillis() - startInitTime} 毫秒")
        }
    }

    /**
     * 添加数据到 SparseArray
     * */
    private fun <T : BaseCoreFw> addToArray(sparse: SparseArray<ArrayList<T>>, t: T) {
        var list = sparse[t.priority()]
        if (list == null) {
            list = ArrayList()
            sparse.put(t.priority(), list)
        }
        list.add(t)
    }

    /**
     * 初始化 config
     * */
    private fun initConfig(
        sparse: SparseArray<ArrayList<CsBaseConfig>>,
        application: Application,
        isDebug: Boolean
    ) {
        for (index in 0 until sparse.size()) {
            val priority = sparse.keyAt(index)
            sparse.get(priority).forEach {
                var time: Long = 0
                if (isDebug) {
                    time = System.currentTimeMillis()
                }
                try {
                    it.create(application, isDebug)
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
                if (isDebug) {
                    CsLogger.tag(TAG)
                        .d("${it.javaClass.simpleName} onCreate 耗时 ${System.currentTimeMillis() - time} 毫秒")
                }
            }
        }
    }

    /**
     * 初始化 application
     * */
    private fun initApplication(
        sparse: SparseArray<ArrayList<CsBaseApplication>>,
        application: Application,
        isDebug: Boolean
    ) {
        for (index in 0 until sparse.size()) {
            val priority = sparse.keyAt(index)
            CsLogger.tag(TAG).d("run priority = $priority")
            sparse.get(priority).forEach {
                var time: Long = 0
                if (isDebug) {
                    time = System.currentTimeMillis()
                }
                try {
                    it.create(application, isDebug)
                } catch (throwable: Throwable) {
                    CsLogger.tag(TAG).e(throwable)
                }
                if (isDebug) {
                    CsLogger.tag(TAG)
                        .d("${it.javaClass.simpleName} onCreate 耗时 ${System.currentTimeMillis() - time} 毫秒")
                }
            }
        }
    }

}