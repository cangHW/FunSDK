package com.proxy.service.funsdk.widget.state

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWidgetStatePageBinding
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.widget.info.toast.CsToast
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/12/24 10:02
 * @desc:
 */
class StatePageActivity : BaseActivity<ActivityWidgetStatePageBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, StatePageActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityWidgetStatePageBinding {
        return ActivityWidgetStatePageBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.show_loading -> {
                statePage?.showLoading()

                CsTask.delay(3, TimeUnit.SECONDS)
                    ?.mainThread()
                    ?.doOnNext(object : IConsumer<Long> {
                        override fun accept(value: Long) {
                            statePage?.hideLoading()
                        }
                    })?.start()
            }

            R.id.show_empty -> {
                statePage?.showEmpty()

                CsTask.delay(3, TimeUnit.SECONDS)
                    ?.mainThread()
                    ?.doOnNext(object : IConsumer<Long> {
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
        }
    }
}