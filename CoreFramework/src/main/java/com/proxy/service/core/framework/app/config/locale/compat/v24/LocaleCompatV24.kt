package com.proxy.service.core.framework.app.config.locale.compat.v24

import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import com.proxy.service.core.framework.app.config.locale.compat.controller.LocaleController
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2025/3/20 14:16
 * @desc:
 */
@RequiresApi(Build.VERSION_CODES.N)
class LocaleCompatV24: LocaleController {

    override fun getCurrentLocal(config: Configuration): Locale {
        return config.locales.get(0)
    }

    override fun setCurrentLocal(config: Configuration, locale: Locale) {
        val set = linkedSetOf(locale)

        val defaultLocales = LocaleList.getDefault()
        val all = List<Locale>(defaultLocales.size()) { defaultLocales.get(it) }
        set.addAll(all)

        config.setLocales(LocaleList(*set.toTypedArray()))
    }
}