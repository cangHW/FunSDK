package com.proxy.service.core

import android.app.Application
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.application.base.BaseCoreFw
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.application.startup.PriorityUtils
import com.proxy.service.core.application.startup.StartupManager
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


/**
 * 整体架构初始化入口
 *
 * @author: cangHX
 * @data: 2024/4/28 18:14
 * @desc:
 */
object CsCore {

    private const val DEFAULT_TIMEOUT: Long = 5 * 1000
    private val isInit = AtomicBoolean(false)

    /**
     * 核心库初始化
     * */
    fun init(application: Application, isDebug: Boolean) {
        init(application, isDebug, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
    }

    /**
     * 核心库初始化并设置超时时长
     * */
    fun init(application: Application, isDebug: Boolean, time: Long, unit: TimeUnit) {
        init(application, isDebug, time, unit, false)
    }

    /**
     * 核心库初始化并设置超时是否需要抛出异常, 如果不抛出异常, 超时后初始化任务会转为后台继续执行.
     * */
    fun init(application: Application, isDebug: Boolean, shouldThrow: Boolean) {
        init(application, isDebug, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS, shouldThrow)
    }

    /**
     * 核心库初始化并设置超时时长与超时是否需要抛出异常, 如果不抛出异常, 超时后初始化任务会转为后台继续执行.
     * */
    fun init(
        application: Application,
        isDebug: Boolean,
        time: Long,
        unit: TimeUnit,
        shouldThrow: Boolean
    ) {
        if (!isInit.compareAndSet(false, true)) {
            CsLogger.tag(StartupManager.TAG).d("init is ready.")
            return
        }

        var startInitTime: Long = 0
        if (isDebug) {
            startInitTime = System.currentTimeMillis()
        }

        CloudSystem.init(application, isDebug)
        CoreConfig.isDebug = isDebug

        val allApp = HashMap<Class<*>, CsBaseApplication>()
        val applications = ArrayList<CsBaseApplication>()
        val configs = ArrayList<CsBaseConfig>()

        CloudSystem.getServices(BaseCoreFw::class.java).forEach {
            PriorityUtils.checkPriority(it)
            when (it) {
                is CsBaseApplication -> {
                    allApp.put(it.javaClass, it)
                    applications.add(it)
                }

                is CsBaseConfig -> {
                    configs.add(it)
                }

                else -> {
                    // do nothing
                }
            }
        }

        initConfig(configs, application, isDebug)

        CsLogger.tag(StartupManager.TAG).d("start init application.")

        StartupManager.getInstance().initializer(
            allApp,
            applications,
            application,
            isDebug,
            time,
            unit,
            shouldThrow
        )

        if (isDebug) {
            CsLogger.tag(StartupManager.TAG)
                .d("CsCore init finish. total usage time ${System.currentTimeMillis() - startInitTime}ms")
        }
    }


    /**
     * 初始化 config
     * */
    private fun initConfig(
        configs: ArrayList<CsBaseConfig>,
        application: Application,
        isDebug: Boolean
    ) {
        configs.sortWith { c1: CsBaseConfig, c2: CsBaseConfig ->
            c1.priority().compareTo(c2.priority())
        }

        configs.forEach {
            var time: Long = 0
            if (isDebug) {
                time = System.currentTimeMillis()
            }
            it.create(application, isDebug)
            if (isDebug) {
                CsLogger.tag(StartupManager.TAG)
                    .d("${it.javaClass.simpleName} onCreate usage time ${System.currentTimeMillis() - time}ms")
            }
        }
    }

}