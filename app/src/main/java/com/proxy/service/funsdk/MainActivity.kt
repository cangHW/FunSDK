package com.proxy.service.funsdk

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.CsCore
import com.proxy.service.core.framework.log.CsLogger
import com.proxy.service.core.framework.log.LogCallback
import com.proxy.service.core.framework.log.LogPriority
import com.proxy.service.funsdk.apihttp.ApiHttpActivity
import com.proxy.service.funsdk.imageloader.ImageLoaderActivity
import com.proxy.service.funsdk.threadpool.ThreadPoolActivity
import com.proxy.service.funsdk.webview.WebViewActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CsCore.init(application, true)

        CsLogger.addLogCallback(object : LogCallback {
            override fun onLog(priority: LogPriority, tag: String, message: String, t: Throwable?) {
                if (priority == LogPriority.DEBUG){
//                    Log.i(tag, message)
                }
            }
        })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.image_loader -> {
                ImageLoaderActivity.launch(this)
            }

            R.id.thread_pool -> {
                ThreadPoolActivity.launch(this)
            }

            R.id.api_http -> {
                ApiHttpActivity.launch(this)
            }

            R.id.web_view -> {
                WebViewActivity.launch(this)
            }
        }
    }
}
