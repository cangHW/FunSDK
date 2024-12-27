package com.proxy.service.core.framework.app.config.locale

import android.content.Context
import com.proxy.service.core.framework.app.config.base.BaseAction
import com.proxy.service.core.framework.app.config.base.ILocale
import com.proxy.service.core.framework.app.config.controller.ConfigurationManager
import com.proxy.service.core.framework.app.config.controller.RefreshManager
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2024/12/24 17:58
 * @desc:
 */
object LocaleImpl : BaseAction(), ILocale {

    override fun save(isSave: Boolean): ILocale {
        setSave(isSave)
        return this
    }

    override fun setLocale(
        context: Context,
        language: String,
        country: String,
        variant: String
    ) {
        setLocale(context, Locale(language, country, variant))
    }

    override fun setLocale(context: Context, locale: Locale) {
        isSave().let {
            LocaleStore.setSave(locale, it)
            LocaleStore.setFollowSystemLocale(false, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }

    override fun setFollowSystemLocale(context: Context) {
        isSave().let {
            LocaleStore.setFollowSystemLocale(true, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }
}