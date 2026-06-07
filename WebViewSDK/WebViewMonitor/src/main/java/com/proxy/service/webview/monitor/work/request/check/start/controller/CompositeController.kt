package com.proxy.service.webview.monitor.work.request.check.start.controller

import com.proxy.service.webview.monitor.work.request.bean.RequestStartData

/**
 * @author: cangHX
 * @date: 2026/6/7 17:11
 * @desc:
 */
class CompositeController(
    private vararg val controllers: AbstractController
) : AbstractController() {

    override fun getKeepTime(): Long {
        var time = 0L
        controllers.forEach {
            val t = it.getKeepTime()
            if (t > time) {
                time = t
            }
        }
        return time
    }

    override fun reset() {
        controllers.forEach {
            it.reset()
        }
    }

    override fun clearCache() {
        controllers.forEach {
            it.clearCache()
        }
    }

    override fun check(time: Float, data: RequestStartData) {
        controllers.forEach {
            if ((time - (data.startTime ?: 0f)) > it.getKeepTime()) {
                return@forEach
            }
            it.check(time, data)
        }
    }

    override fun report() {
        controllers.forEach {
            it.report()
        }
    }
}