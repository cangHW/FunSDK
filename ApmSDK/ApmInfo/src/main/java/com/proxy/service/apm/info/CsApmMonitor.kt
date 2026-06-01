package com.proxy.service.apm.info

import android.os.Handler
import android.os.Looper
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.crash.native_crash.jni.NativeCrashBridge
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * Apm 稳定性监控框架全局入口。
 *
 * @author: cangHX
 * @date: 2025/4/22 17:38
 */
object CsApmMonitor {

    private const val TAG = "${Constants.TAG}Test"

    private val lock = Any()
    private val mainHandler = Handler(Looper.getMainLooper())

    private var config: ApmConfig = ApmConfig.builder().build()

    /**
     * 注入 Apm 配置（Crash、主线程慢分发、UI 卡顿等）。
     */
    fun setConfig(config: ApmConfig) {
        this.config = config
    }

    fun getConfig(): ApmConfig {
        return config
    }


    /**
     * 测试主线程卡顿
     * */
    fun testMainThreadLag() {
        mainHandler.post {
            try {
                Thread.sleep(1500)
            } catch (_: InterruptedException) {
            }
        }
    }

    /**
     * 测试 java crash
     * */
    fun testJavaCrash() {
        throw NullPointerException("test java crash")
    }

    /**
     * 测试 native crash
     *
     * 1: SIGSEGV: 空指针解引用,
     * 2: SIGABRT: 主动 abort,
     * 3: SIGSEGV: 非法地址写入
     * 其他：默认：空指针
     * */
    fun testNativeCrash(type: Int = 1) {
        NativeCrashBridge.nativeTestCrash(type)
    }


    /**
     * 测试 ANR：直接在主线程 sleep 10s，触发系统 ANR 判定。
     * 需要在此期间点击屏幕产生 InputDispatch 超时。
     * */
    fun testAnr() {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                synchronized(lock) {
                    mainHandler.post {
                        synchronized(lock) {
                            CsLogger.tag(TAG).i("main thread run.")
                        }
                    }

                    Thread.sleep(10 * 1000)
                }
                return ""
            }
        })?.start()

    }

}