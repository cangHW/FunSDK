package com.proxy.service.core.application.base

import android.app.Application
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * 基类 Application，用于多 module 初始化
 *
 * @author: cangHX
 * @data: 2024/4/28 18:21
 * @desc:
 */
abstract class CsBaseApplication : BaseCoreFw() {

    companion object {
        private const val TAG = "${CoreConfig.TAG}Application"
    }

    enum class ThreadType {
        /**
         * io 线程
         * */
        IO_THREAD,

        /**
         * cpu 线程
         * */
        COMPUTATION_THREAD,

        /**
         * 主线程
         * */
        MAIN_THREAD;
    }

    /**
     * 前置依赖任务
     * */
    open fun dependsOn(): List<Class<out CsBaseApplication>>? {
        return null
    }

    /**
     * 运行线程
     * */
    open fun runOnThread(): ThreadType {
        return ThreadType.MAIN_THREAD
    }


    final override fun create(application: Application, isDebug: Boolean) {
        CsLogger.tag(TAG).d("${this.javaClass.simpleName} start onCreate. isDebug = $isDebug")
        onCreate(application, isDebug)
    }

}