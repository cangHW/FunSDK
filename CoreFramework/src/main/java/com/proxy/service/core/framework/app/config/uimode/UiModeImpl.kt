package com.proxy.service.core.framework.app.config.uimode

import android.content.Context
import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.base.AppConfigConstants
import com.proxy.service.core.framework.app.config.base.BaseAction
import com.proxy.service.core.framework.app.config.base.IUiMode
import com.proxy.service.core.framework.app.config.controller.AppConfigInit
import com.proxy.service.core.framework.app.config.controller.ConfigurationManager
import com.proxy.service.core.framework.app.config.controller.RefreshManager
import com.proxy.service.core.framework.io.sp.CsSpManager

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
            UiModeStore.setFollowSystem(true, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()

        CsSpManager.name(AppConfigConstants.APP_CONFIG_SP_NAME)
            .put(AppConfigConstants.APP_CONFIG_UI_MODE, false)
        AppConfigInit.finish()
    }

    override fun openNightMode(context: Context) {
        isSave().let {
            UiModeStore.setSave(Configuration.UI_MODE_NIGHT_YES, it)
            UiModeStore.setFollowSystem(false, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }

    override fun closeNightMode(context: Context) {
        isSave().let {
            UiModeStore.setSave(Configuration.UI_MODE_NIGHT_NO, it)
            UiModeStore.setFollowSystem(false, it)
        }

        ConfigurationManager.applyConfiguration(context)
        RefreshManager.refreshNow()
    }
}