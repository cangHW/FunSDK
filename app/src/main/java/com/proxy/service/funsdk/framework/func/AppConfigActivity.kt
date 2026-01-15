package com.proxy.service.funsdk.framework.func

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.framework.app.config.CsConfigUtils
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityFrameworkAppconfigBinding
import java.util.Locale


/**
 * @author: cangHX
 * @data: 2024/12/25 16:18
 * @desc:
 */
class AppConfigActivity : BaseActivity<ActivityFrameworkAppconfigBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, AppConfigActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityFrameworkAppconfigBinding {
        return ActivityFrameworkAppconfigBinding.inflate(inflater)
    }

    override fun onClick(view: View) {
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