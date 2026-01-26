package com.proxy.service.webview.monitor.callback

/**
 * @author: cangHX
 * @data: 2026/1/26 12:43
 * @desc:
 */
interface MonitorCallback {

    /**
     * 检测数据回调
     * */
    fun onMonitorCall(url: String, log: String)

}