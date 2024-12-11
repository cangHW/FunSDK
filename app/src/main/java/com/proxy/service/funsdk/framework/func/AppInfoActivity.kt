package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2024/12/11 10:15
 * @desc:
 */
class AppInfoActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AppInfoActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_appinfo)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.app_night_mode -> {
                Toast.makeText(
                    this,
                    "是否处于暗夜模式: ${CsAppUtils.isNightMode()}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}