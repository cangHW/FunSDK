package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.ui.view.action.CsViewAction
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkUiBinding
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2026/1/8 17:47
 * @desc:
 */
class UiActivity : BaseActivity<ActivityFrameworkUiBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, UiActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkUiBinding {
        return ActivityFrameworkUiBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
        CsViewAction.click(binding?.deDoubleClick)
            .setDebounceTime(500, TimeUnit.MILLISECONDS)
            .call {
                binding?.content?.addData("防双击", "点击")
            }

        CsViewAction.longClick(binding?.longClick)
            .callOnly {
                binding?.content?.addData("长按", "长按")
            }
    }

    override fun onClick(view: View) {

    }
}