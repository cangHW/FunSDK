package com.proxy.service.funsdk.apm

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityApmJankTestBinding

/**
 * 主线程慢分发手工验证页。
 *
 * 点击按钮后在主线程 Handler 中 sleep 1.5s，用于触发 ApmInfo jank 监控并检查
 * `getExternalFilesDir("apm")/performance/main_thread_lag/` 下的 `jank_*.txt`。
 */
class ApmActivity : BaseActivity<ActivityApmJankTestBinding>() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ApmActivity::class.java))
        }
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun getViewBinding(inflater: LayoutInflater): ActivityApmJankTestBinding {
        return ActivityApmJankTestBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.main_thread_lag_trigger -> {
                binding?.jankHint?.text = "正在主线程 sleep 1500ms..."

                CsApmMonitor.testMainThreadLag()

                mainHandler.post {
                    binding?.jankHint?.text = "完成，请查看 apm/performance/main_thread_lag/ 目录"
                }
            }

            R.id.java_crash_trigger -> {
                CsApmMonitor.testJavaCrash()
            }

            R.id.native_crash_trigger -> {
                CsApmMonitor.testNativeCrash()
            }

            R.id.anr_trigger -> {
                binding?.jankHint?.text = "主线程将 sleep 10s，请快速点击屏幕触发 ANR..."
                CsApmMonitor.testAnr()
            }
        }
    }
}
