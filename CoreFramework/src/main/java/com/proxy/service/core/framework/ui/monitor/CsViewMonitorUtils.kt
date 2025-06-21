package com.proxy.service.core.framework.ui.monitor

import com.proxy.service.core.framework.ui.monitor.visible.base.IVisibleMonitorHelper
import com.proxy.service.core.framework.ui.monitor.visible.callback.VisibleMonitorCallback
import com.proxy.service.core.framework.ui.monitor.visible.config.VisibleMonitorConfig
import com.proxy.service.core.framework.ui.monitor.visible.impl.VisibleMonitorHelperImpl

/**
 * @author: cangHX
 * @data: 2024/12/4 18:27
 * @desc:
 */
object CsViewMonitorUtils {

    /**
     * 创建 view 显示状态监听器
     * */
    fun createVisibleMonitor(config: VisibleMonitorConfig, callback: VisibleMonitorCallback): IVisibleMonitorHelper {
        return VisibleMonitorHelperImpl(config, callback)
    }

}