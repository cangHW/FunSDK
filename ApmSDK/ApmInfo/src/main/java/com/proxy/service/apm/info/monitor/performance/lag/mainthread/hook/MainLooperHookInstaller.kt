package com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook

import android.os.Looper
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractHook
import com.proxy.service.apm.info.monitor.base.AbstractInstaller
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.observer.ObserverHook
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.hook.printer.PrinterHook
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 主 Looper Hook 安装器：API 29+ 优先 [ObserverHook]，失败或低版本降级 [PrinterHook]。
 *
 * @author: cangHX
 * @date: 2026/5/22 17:27
 */
class MainLooperHookInstaller(
    private val listener: DispatchListener
): AbstractInstaller() {

    companion object {
        private const val TAG = "${Constants.TAG}MainLooperHookInstaller"
    }

    private val looper = Looper.getMainLooper()
    private val installed = AtomicBoolean(false)
    private var activeHook: AbstractHook<Looper>? = null

    override fun install() {
        if (installed.compareAndSet(false, true)) {
            var flag = false
            try {
                activeHook = createHook()
                flag = activeHook?.start(looper) ?: false
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).w("Observer hook failed, fallback to PrinterHook")
            }

            if (!flag) {
                activeHook = PrinterHook(listener)
                activeHook?.start(looper)
            }
        }
    }

    override fun uninstall() {
        if (installed.compareAndSet(true, false)) {
            activeHook?.stop(looper)
            activeHook = null
        }
    }

    private fun createHook(): AbstractHook<Looper> {
        return PrinterHook(listener)
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            ObserverHook(listener)
//        } else {
//            PrinterHook(listener)
//        }
    }
}