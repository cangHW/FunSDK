package com.proxy.service.core.framework.app.config.locale

import android.content.Context
import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.base.BaseConfigurationCheck
import com.proxy.service.core.framework.app.config.locale.compat.LocaleCompat

/**
 * @author: cangHX
 * @data: 2024/12/25 10:13
 * @desc:
 */
object LocaleCheck : BaseConfigurationCheck() {

    private var defaultLocale = LocaleCompat.getDefaultLocal()

    override fun onSystemConfigurationChanged(newConfig: Configuration) {
        defaultLocale = LocaleCompat.getCurrentLocal(newConfig)
    }

    override fun onConfigurationCheck(context: Context, config: Configuration): Boolean {
        val flag = LocaleStore.isFollowingSystem()
        val locale = if (flag) {
            defaultLocale
        } else {
            LocaleStore.getValue() ?: defaultLocale
        }

        val current = LocaleCompat.getCurrentLocal(config)
        if (current == locale) {
            return false
        }

        LocaleCompat.setCurrentLocal(config, locale)
        return true
    }
}