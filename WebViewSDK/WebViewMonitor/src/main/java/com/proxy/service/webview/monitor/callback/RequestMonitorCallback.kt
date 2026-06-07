package com.proxy.service.webview.monitor.callback

import com.proxy.service.webview.monitor.work.request.bean.RequestStartData

/**
 * @author: cangHX
 * @date: 2026/6/8 11:41
 * @desc:
 */
interface RequestMonitorCallback : MonitorCallback {

    /**
     * H5重复请求预警回调
     * */
    fun onRequestRepetitionCall(list: MutableList<RequestStartData>) {
    }

    /**
     * H5并发请求预警回调
     * */
    fun onRequestStormCall(list: MutableList<RequestStartData>) {
    }

}