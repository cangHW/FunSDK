package com.proxy.service.core.framework.io.monitor

import com.proxy.service.core.framework.io.monitor.callback.FileMonitorCallback
import com.proxy.service.core.framework.io.monitor.manager.FileManager
import com.proxy.service.core.framework.io.monitor.manager.IManager

/**
 * @author: cangHX
 * @data: 2025/4/23 10:49
 * @desc:
 */
object CsFileMonitor {

    fun createMonitor(rootPath: String, callback: FileMonitorCallback): IManager {
        return FileManager.create(rootPath, callback)
    }

}