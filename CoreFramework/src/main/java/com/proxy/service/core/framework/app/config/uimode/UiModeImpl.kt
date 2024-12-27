package com.proxy.service.core.framework.app.config.uimode

import android.content.Context
import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.base.BaseAction
import com.proxy.service.core.framework.app.config.base.IUiMode
import com.proxy.service.core.framework.app.config.controller.ConfigurationManager
import com.proxy.service.core.framework.app.config.controller.RefreshManager

/**
 * @author: cangHX
 * @data: 2024/12/25 20:51
 * @desc:
 */
object UiModeImpl : BaseAction(), IUiMode {

    override fun save(isSave: Boolean): IUiMode {
        setSave(isSave)
        return this
    }

    override fun setFollowSystemMode(context: Context) {
        isSave().let {
            UiModeStore.setFollowSystemLocale(true, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }

    override fun openNightMode(context: Context) {
        isSave().let {
            UiModeStore.setSave(Configuration.UI_MODE_NIGHT_YES, it)
            UiModeStore.setFollowSystemLocale(false, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }

    override fun closeNightMode(context: Context) {
        isSave().let {
            UiModeStore.setSave(Configuration.UI_MODE_NIGHT_NO, it)
            UiModeStore.setFollowSystemLocale(false, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }
}