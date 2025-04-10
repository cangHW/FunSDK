package com.proxy.service.funsdk

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.api.log.base.LogTree
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.funsdk.apihttp.ApiActivity
import com.proxy.service.funsdk.framework.FrameWorkActivity
import com.proxy.service.funsdk.imageloader.ImageLoaderActivity
import com.proxy.service.funsdk.permission.PermissionActivity
import com.proxy.service.funsdk.threadpool.ThreadPoolActivity
import com.proxy.service.funsdk.webview.WebViewActivity
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CsBarUtils.setStatusBarTransparent(this)
        CsBarUtils.setNavigationBarTransparent(this)

        val file = File(getExternalFilesDir(null), "asd.txt")
        CsFileUtils.createFile(file)

        CsLogger.addLogCallback(object : LogTree() {
            override fun onLog(priority: Int, tag: String, message: String, throwable: Throwable?) {
                if (priority == Log.DEBUG) {
//                    Log.i(tag, message)
                }
            }
        })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.core_framework -> {

                throw NullPointerException("ssssss")

                FrameWorkActivity.launch(this)
            }

            R.id.image_loader -> {
                ImageLoaderActivity.launch(this)
            }

            R.id.thread_pool -> {
                ThreadPoolActivity.launch(this)
            }

            R.id.api_http -> {
                ApiActivity.launch(this)
            }

            R.id.web_view -> {
                WebViewActivity.launch(this)
            }

            R.id.permission -> {
                PermissionActivity.launch(this)
            }
        }
    }
}
