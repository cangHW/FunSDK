package com.proxy.service.funsdk.base

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

/**
 * @author: cangHX
 * @data: 2026/2/4 17:59
 * @desc:
 */
object LaunchUtils {

    fun launch(context: Context, aClass: Class<out AppCompatActivity>) {
        val intent = Intent(context, aClass)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

}