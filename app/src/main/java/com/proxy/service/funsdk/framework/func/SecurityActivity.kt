package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.security.CsMd5Utils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.funsdk.R
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2024/11/14 15:13
 * @desc:
 */
class SecurityActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SecurityActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_security)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.md5 -> {
                CsTask.ioThread()?.call(object : ICallable<String> {
                    override fun accept(): String {
                        CsLogger.d(CsMd5Utils.getMD5(resources.openRawResource(R.raw.reader_download_loading)))
                        return ""
                    }
                })?.start()
            }
        }
    }

}