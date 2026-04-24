package com.proxy.service.funsdk.widget

import android.view.LayoutInflater
import android.view.View
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetBinding
import com.proxy.service.funsdk.widget.dialog.DialogActivity
import com.proxy.service.funsdk.widget.notification.NotificationActivity
import com.proxy.service.funsdk.widget.state.StatePageActivity
import com.proxy.service.funsdk.widget.toast.ToastActivity
import com.proxy.service.funsdk.widget.view.centerSelect.CenterSelectActivity


/**
 * @author: cangHX
 * @data: 2025/7/8 17:55
 * @desc:
 */
class WidgetActivity : BaseActivity<ActivityWidgetBinding>() {

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetBinding {
        return ActivityWidgetBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.toast -> {
                ToastActivity.launch(this)
            }

            R.id.state_page -> {
                StatePageActivity.launch(this)
            }

            R.id.dialog -> {
                DialogActivity.launch(this)
            }

            R.id.notification -> {
                NotificationActivity.launch(this)
            }

            R.id.view -> {
                CenterSelectActivity.launch(this)
            }
        }
    }
}