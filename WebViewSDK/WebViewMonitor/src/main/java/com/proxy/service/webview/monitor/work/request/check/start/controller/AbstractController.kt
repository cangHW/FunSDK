package com.proxy.service.webview.monitor.work.request.check.start.controller

import com.proxy.service.webview.monitor.work.request.bean.RequestStartData

/**
 * @author: cangHX
 * @date: 2026/6/7 17:01
 * @desc:
 */
abstract class AbstractController {

    /**
     * 数据保留时间
     * */
    abstract fun getKeepTime(): Long

    /**
     * 重置, 切换页面会进行重置
     * */
    open fun reset() {}

    /**
     * 清理缓存
     * */
    abstract fun clearCache()

    /**
     * 检测
     * */
    abstract fun check(time: Float, data: RequestStartData)

    /**
     * 出结果
     * */
    abstract fun report()
}