package com.proxy.service.apm.info.monitor.performance.leak.reporter

import com.proxy.service.apm.info.config.controller.performance.leak.IMemoryLeakConfigGet
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.toast.CsToast
import com.proxy.service.widget.info.toast.enums.ToastDuration

object MemoryLeakToastHelper {

    private const val TAG = "${Constants.TAG}MemoryLeak"

    @Volatile
    private var lastToastWallMs = 0L

    fun notify(config: IMemoryLeakConfigGet, message: String) {
        CsLogger.tag(TAG).w(message)
        if (!CoreConfig.isDebug || !config.getEnableDebugToast()) {
            return
        }
        val now = System.currentTimeMillis()
        if (now - lastToastWallMs < Constants.MONITOR_MEMORY_LEAK_NOTIFY_THROTTLE_MS) {
            return
        }
        lastToastWallMs = now
        CsToast.show(message, ToastDuration.LENGTH_LONG)
    }
}
