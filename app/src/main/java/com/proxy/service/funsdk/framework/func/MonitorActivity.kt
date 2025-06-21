package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.ui.monitor.CsViewMonitorUtils
import com.proxy.service.core.framework.ui.monitor.visible.base.IVisibleMonitorHelper
import com.proxy.service.core.framework.ui.monitor.visible.callback.VisibleMonitorCallback
import com.proxy.service.core.framework.ui.monitor.visible.config.VisibleMonitorConfig
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkMonitorBinding

/**
 * @author: cangHX
 * @data: 2024/12/5 16:36
 * @desc:
 */
class MonitorActivity : BaseActivity<ActivityFrameworkMonitorBinding>() {

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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.monitor_start -> {
                if (helper == null) {
                    val config = VisibleMonitorConfig.builder(binding?.checkView!!)
                        .setTag("222")
                        .setLifecycle(this)
                        .build()

                    helper = CsViewMonitorUtils.createVisibleMonitor(config, monitorCallback)
                }
                helper?.start()
                binding?.content?.addData("Monitor", "开始监控")
            }

            R.id.monitor_reset -> {
                helper?.reset()
                binding?.content?.addData("Monitor", "重置监控状态")
            }

            R.id.monitor_stop -> {
                helper?.stop()
                binding?.content?.addData("Monitor", "暂停监控")
            }

            R.id.monitor_destroy -> {
                helper?.destroy()
                helper = null
                binding?.content?.addData("Monitor", "关闭监控")
            }

            R.id.view_visible -> {
                binding?.checkView?.visibility = View.VISIBLE
            }

            R.id.view_gone -> {
                binding?.checkView?.visibility = View.GONE
            }
        }
    }

    private val monitorCallback = object : VisibleMonitorCallback{
        override fun onGone(tag: Any?) {
            binding?.content?.addData("Monitor", "view 隐藏, tag = $tag")
        }

        override fun onShow(tag: Any?) {
            binding?.content?.addData("Monitor", "view 显示, tag = $tag")
        }
    }

}