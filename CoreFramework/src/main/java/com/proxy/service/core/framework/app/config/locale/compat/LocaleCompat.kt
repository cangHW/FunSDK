package com.proxy.service.core.framework.app.config.locale.compat

import android.content.res.Configuration
import android.os.Build
import com.proxy.service.core.framework.app.config.locale.compat.controller.LocaleController
import com.proxy.service.core.framework.app.config.locale.compat.v23.LocaleCompatV23
import com.proxy.service.core.framework.app.config.locale.compat.v24.LocaleCompatV24
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2025/3/20 11:55
 * @desc:
 */
object LocaleCompat : LocaleController {

    private val controller: LocaleController = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocaleCompatV24()
    } else {
        LocaleCompatV23()
    }

    fun getDefaultLocal(): Locale {
        return Locale.getDefault()
    }

    override fun getCurrentLocal(config: Configuration): Locale {
        return controller.getCurrentLocal(config)
    }

    override fun setCurrentLocal(config: Configuration, locale: Locale) {
        Locale.setDefault(locale)
        controller.setCurrentLocal(config, locale)
    }

}