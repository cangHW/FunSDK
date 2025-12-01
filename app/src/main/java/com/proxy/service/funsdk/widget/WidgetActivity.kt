package com.proxy.service.funsdk.widget

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetBinding
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.widget.info.toast.CsToast
import java.util.concurrent.TimeUnit


/**
 * @author: cangHX
 * @data: 2025/7/8 17:55
 * @desc:
 */
class WidgetActivity: BaseActivity<ActivityWidgetBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, WidgetActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetBinding {
        return ActivityWidgetBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.toast -> {
                CsToast.show("测试")
            }

            R.id.show_loading -> {
                statePage?.showLoading()

                CsTask.delay(3, TimeUnit.SECONDS)
                    ?.mainThread()
                    ?.doOnNext(object : IConsumer<Long>{
                        override fun accept(value: Long) {
                            statePage?.hideLoading()
                        }
                    })?.start()
            }

            R.id.show_empty -> {
                statePage?.showEmpty()

                CsTask.delay(3, TimeUnit.SECONDS)
                    ?.mainThread()
                    ?.doOnNext(object : IConsumer<Long>{
                        override fun accept(value: Long) {
                            statePage?.hideEmpty()
                        }
                    })?.start()
            }

            R.id.show_error -> {
                statePage?.showError {
                    CsToast.show("错误页面点击")

                    statePage?.hideError()
                }
            }

            R.id.show_dialog -> {
//                if (!Settings.canDrawOverlays(this)) {
//                    val intent = Intent(
//                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + CsAppUtils.getPackageName())
//                    )
//                    startActivityForResult(intent, 100)
//                }else {
//                    TestDialog(0).show()
//                }

                for (index in 0..3) {
                    TestDialog(index).show(this)
                }
            }
        }
    }
}