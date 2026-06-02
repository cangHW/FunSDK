package com.proxy.service.funsdk.apm

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.apm.info.CsApmMonitor
import com.proxy.service.apm.info.CsApmMonitorTest
import com.proxy.service.apm.info.cache.ExceptionHandler
import com.proxy.service.core.framework.data.json.CsJsonUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.monitor.info.FileInfo
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityApmJankTestBinding

/**
 * apm 检测。
 */
class ApmActivity : BaseActivity<ActivityApmJankTestBinding>() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, ApmActivity::class.java))
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityApmJankTestBinding {
        return ActivityApmJankTestBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.total_log_file -> {
                CsApmMonitor.setExceptionHandler(object : ExceptionHandler {
                    override fun onException(filePaths: List<FileInfo>) {
                        val json = CsJsonUtils.toJson(filePaths)
                        CsLogger.tag("ExceptionHandler").e("filePaths=$json")
                        binding?.content?.addData("全部异常信息文件", json)
                    }
                })
            }

            R.id.main_thread_lag_trigger -> {
                binding?.content?.addData("检测", "测试主线程卡顿")
                CsApmMonitorTest.testMainThreadLag()
            }

            R.id.java_crash_trigger -> {
                binding?.content?.addData("检测", "测试 java crash")
                CsApmMonitorTest.testJavaCrash()
            }

            R.id.native_crash_trigger -> {
                binding?.content?.addData("检测", "测试 native crash")
                CsApmMonitorTest.testNativeCrash()
            }

            R.id.anr_trigger -> {
                binding?.content?.addData("检测", "测试 anr")
                CsApmMonitorTest.testAnr()
            }
        }
    }
}
