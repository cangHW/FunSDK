package com.proxy.service.webview.monitor.work.base

import com.proxy.service.webview.monitor.work.cookie.CookieMonitor
import com.proxy.service.webview.monitor.work.load.page.LoadPageMonitor
import com.proxy.service.webview.monitor.work.load.res.LoadResourceMonitor
import com.proxy.service.webview.monitor.work.request.RequestMonitor

/**
 * @author: cangHX
 * @date: 2026/6/7 10:10
 * @desc:
 */
class MonitorFactory {

    private val monitorMap = HashMap<Class<*>, BaseMonitor>()

    @Suppress("UNCHECKED_CAST")
    private fun <T : BaseMonitor> getMonitor(tClass: Class<T>): T {
        var monitor = monitorMap[tClass]
        if (monitor == null) {
            synchronized(monitorMap) {
                monitor = monitorMap[tClass]
                if (monitor == null) {
                    val finalMonitor = tClass.getDeclaredConstructor().newInstance()
                    monitorMap[tClass] = finalMonitor
                    monitor = finalMonitor
                }
            }
        }
        return monitor as T
    }

    fun getCookieMonitor(): CookieMonitor {
        return getMonitor(CookieMonitor::class.java)
    }

    fun getRequestMonitor(): RequestMonitor {
        return getMonitor(RequestMonitor::class.java)
    }

    fun getLoadPageMonitor(): LoadPageMonitor {
        return getMonitor(LoadPageMonitor::class.java)
    }

    fun getLoadResourceMonitor(): LoadResourceMonitor {
        return getMonitor(LoadResourceMonitor::class.java)
    }

}