package com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook

import android.os.Looper

/**
 * @author: cangHX
 * @date: 2026/5/22 17:32
 * @desc:
 */
abstract class AbstractHook {

    abstract fun install(looper: Looper): Boolean

    abstract fun uninstall(looper: Looper)

}