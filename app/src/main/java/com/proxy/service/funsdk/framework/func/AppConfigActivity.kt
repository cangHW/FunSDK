package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.app.config.CsConfigUtils
import com.proxy.service.funsdk.R
import java.util.Locale


/**
 * @author: cangHX
 * @data: 2024/12/25 16:18
 * @desc:
 */
class AppConfigActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AppConfigActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var uiModeManager:UiModeManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_framework_appconfig)
        uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun onClick(view: View) {
        when (view.id) {
            R.id.app_follow_system_mode -> {
                CsConfigUtils.getLocaleManager().setFollowSystemLocale(this)
                CsConfigUtils.getUiModeManager().setFollowSystemMode(this)
            }

            R.id.app_englist_mode -> {
                CsConfigUtils.getLocaleManager().setLocale(this, Locale.ENGLISH)
            }

            R.id.app_chinese_mode -> {
                CsConfigUtils.getLocaleManager().setLocale(this, Locale.CHINESE)
            }

            R.id.app_day_mode -> {
                CsConfigUtils.getUiModeManager().closeNightMode(this)
            }

            R.id.app_night_mode -> {
                CsConfigUtils.getUiModeManager().openNightMode(this)
            }
        }
    }

}