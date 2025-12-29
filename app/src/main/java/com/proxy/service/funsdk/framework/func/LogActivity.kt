package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkLogBinding

/**
 * @author: cangHX
 * @data: 2025/11/17 11:13
 * @desc:
 */
class LogActivity : BaseActivity<ActivityFrameworkLogBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, LogActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkLogBinding {
        return ActivityFrameworkLogBinding.inflate(inflater)
    }

    private val logPath =
        "/storage/emulated/0/Android/data/com.proxy.service.funsdk/files/logfile/log_2025-11-17.log"
    private val outPath =
        "/storage/emulated/0/Android/data/com.proxy.service.funsdk/files/log_decompress.log"

    override fun onClick(view: View) {
        when (view.id) {
            R.id.log -> {
//                val result = LogFileDecompress.getInstance().decompressLogFile(
//                    logPath,
//                    outPath,
//                    "111222"
//                )
//                CsToast.show("解密 $result")

                val time = System.currentTimeMillis()
                for (index in 0..1000) {
                    CsLogger.d("测试 $index")
                }

                CsLogger.e("耗时 ${System.currentTimeMillis() - time}")

            }
        }
    }

}