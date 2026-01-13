package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.ui.view.action.CsViewAction
import com.proxy.service.core.framework.ui.view.action.base.IViewActionCallback
import com.proxy.service.core.framework.ui.view.action.exposure.controller.ExposureController
import com.proxy.service.core.framework.ui.view.action.exposure.params.ExposureParams
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

    private var helper: ExposureController? = null

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkMonitorBinding {
        return ActivityFrameworkMonitorBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.monitor_start -> {
                if (helper == null) {
                    helper = CsViewAction.exposure(binding?.checkView)
                        .setTag("222")
                        .call(viewActionCallback)
                }
                helper?.start()
                binding?.content?.addData("Monitor", "开始检测")
            }

            R.id.monitor_reset -> {
                helper?.reset()
                binding?.content?.addData("Monitor", "重置检测状态")
            }

            R.id.monitor_stop -> {
                helper?.stop()
                binding?.content?.addData("Monitor", "暂停检测")
            }

            R.id.monitor_destroy -> {
                helper?.release()
                helper = null
                binding?.content?.addData("Monitor", "关闭检测")
            }

            R.id.view_visible -> {
                binding?.checkView?.visibility = View.VISIBLE
            }

            R.id.view_gone -> {
                binding?.checkView?.visibility = View.GONE
            }
        }
    }

    private val viewActionCallback = IViewActionCallback<ExposureParams> { value ->
        if (value.isExposureState()) {
            binding?.content?.addData("Monitor", "view 显示, tag = ${value.getTag()}")
        } else {
            binding?.content?.addData("Monitor", "view 隐藏, tag = ${value.getTag()}")
        }
    }

}