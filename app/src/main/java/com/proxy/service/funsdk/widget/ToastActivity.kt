package com.proxy.service.funsdk.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetToastBinding
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2025/12/24 10:06
 * @desc:
 */
class ToastActivity : BaseActivity<ActivityWidgetToastBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ToastActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetToastBinding {
        return ActivityWidgetToastBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.toast -> {
                CsToast.show("测试")
            }
        }
    }
}