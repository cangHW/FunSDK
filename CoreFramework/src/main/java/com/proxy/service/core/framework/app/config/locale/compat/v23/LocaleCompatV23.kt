package com.proxy.service.core.framework.app.config.locale.compat.v23

import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.locale.compat.controller.LocaleController
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2025/3/20 14:15
 * @desc:
 */
class LocaleCompatV23 : LocaleController {
    override fun getCurrentLocal(config: Configuration): Locale {
        return config.locale
    }

    override fun setCurrentLocal(config: Configuration, locale: Locale) {
        config.setLocale(locale)
    }
}