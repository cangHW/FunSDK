package com.proxy.service.core.framework.ui.view.action.exposure.controller

/**
 * @author: cangHX
 * @data: 2026/1/9 14:22
 * @desc:
 */
interface ExposureController {

    /**
     * 开始检测
     */
    fun start()

    /**
     * 还原数据，重置为原始状态, 需要重新调用 [start]
     */
    fun reset()

    /**
     * 暂停检测
     */
    fun stop()

    /**
     * 释放检测
     */
    fun release()

}