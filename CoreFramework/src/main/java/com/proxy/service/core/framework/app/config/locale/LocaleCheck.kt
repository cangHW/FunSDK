package com.proxy.service.core.framework.app.config.locale

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import com.proxy.service.core.framework.app.config.base.BaseConfigurationCheck
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2024/12/25 10:13
 * @desc:
 */
object LocaleCheck : BaseConfigurationCheck() {

    private var defaultLocale = Locale.getDefault()

    override fun onSystemConfigurationChanged(newConfig: Configuration) {
        defaultLocale = getLocale(newConfig)
    }

    override fun onConfigurationCheck(context: Context, config: Configuration): Boolean {
        val flag = LocaleStore.isFollowingSystem()
        val locale = if (flag) {
            defaultLocale
        } else {
            LocaleStore.getValue() ?: defaultLocale
        }

        val current = getLocale(config)
        if (current == locale) {
            return false
        }

        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setLocaleForApi24(config, locale)
        } else {
            config.setLocale(locale)
        }
        return true
    }

    private fun getLocale(config: Configuration): Locale {
        return config.locales.get(0)
    }

    private fun setLocaleForApi24(config: Configuration, locale: Locale) {
        val set = linkedSetOf(locale)

        val defaultLocales = LocaleList.getDefault()
        val all = List<Locale>(defaultLocales.size()) { defaultLocales.get(it) }
        set.addAll(all)

        config.setLocales(LocaleList(*set.toTypedArray()))
    }
}