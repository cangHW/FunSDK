package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.ui.monitor.CsViewMonitorUtils
import com.proxy.service.core.framework.ui.monitor.visible.base.IVisibleMonitorHelper
import com.proxy.service.core.framework.ui.monitor.visible.config.VisibleMonitorConfig
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/12/5 16:36
 * @desc:
 */
class MonitorActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, MonitorActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var helper: IVisibleMonitorHelper? = null
    private var checkView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)

        checkView = findViewById(R.id.view)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.monitor_start -> {
                if (helper == null) {
                    val config = VisibleMonitorConfig.builder(checkView!!)
                        .setTag("222")
                        .setLifecycle(this)
                        .build()

                    helper = CsViewMonitorUtils.createVisibleMonitor(config)
                }
                helper?.start()
            }

            R.id.monitor_reset -> {
                helper?.reset()
            }

            R.id.monitor_stop -> {
                helper?.stop()
            }

            R.id.monitor_destroy -> {
                helper?.destroy()
                helper = null
            }

            R.id.view_visible -> {
                checkView?.visibility = View.VISIBLE
            }

            R.id.view_gone -> {
                checkView?.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        System.gc()
    }

}