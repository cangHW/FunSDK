package com.proxy.service.apm.info.test

import android.app.Activity
import android.os.Bundle
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.apm.info.CsApmMonitorTest

/**
 * @author: cangHX
 * @date: 2026/6/5 11:10
 * @desc:
 */
class TestLeakActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            if (CsApmMonitor.getConfig().getMemoryLeakMonitorConfig().getEnable()) {
                CsApmMonitorTest.LeakTestHolder.leakedObjects.add(this)
            }
        } catch (_: Throwable) {
        } finally {
            finish()
        }
    }

}