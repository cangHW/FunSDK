package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.worker.CsWorkUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.framework.func.work.TestWork

/**
 * @author: cangHX
 * @data: 2024/12/16 17:24
 * @desc:
 */
class WorkActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, WorkActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_work)
        CsLogger.d("sssssssssss")
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.start_work_task -> {
                CsWorkUtils.start(TestWork::class.java)
            }

            R.id.cancel_work_task -> {
                CsWorkUtils.cancel(TestWork::class.java)
            }
        }
    }

}