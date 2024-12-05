package com.proxy.service.core.framework.ui.monitor.visible.base

/**
 * @author: cangHX
 * @data: 2024/12/4 20:15
 * @desc:
 */
interface IVisibleMonitorHelper {

    /**
     * 开始
     */
    fun start()

    /**
     * 还原数据，重置为原始状态
     */
    fun reset()

    /**
     * 暂停
     */
    fun stop()

    /**
     * 销毁
     */
    fun destroy()

}