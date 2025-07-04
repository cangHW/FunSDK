package com.proxy.service.core.framework.app.config

import com.proxy.service.core.framework.app.config.base.AppConfigConstants
import com.proxy.service.core.framework.app.config.base.ILocale
import com.proxy.service.core.framework.app.config.base.IUiMode
import com.proxy.service.core.framework.app.config.controller.AppConfigInit
import com.proxy.service.core.framework.app.config.locale.LocaleImpl
import com.proxy.service.core.framework.app.config.uimode.UiModeImpl
import com.proxy.service.core.framework.io.sp.CsSpManager

/**
 * 应用配置工具类
 *
 * @author: cangHX
 * @data: 2024/12/24 17:45
 * @desc:
 */
object CsConfigUtils {

    /**
     * 是否修改过配置
     * */
    fun isConfigHasChange(): Boolean {
        val isUiModeChange = CsSpManager.name(AppConfigConstants.APP_CONFIG_SP_NAME)
            .getBoolean(AppConfigConstants.APP_CONFIG_UI_MODE, false)
        val isLocaleChange = CsSpManager.name(AppConfigConstants.APP_CONFIG_SP_NAME)
            .getBoolean(AppConfigConstants.APP_CONFIG_LOCALE, false)
        return isUiModeChange || isLocaleChange
    }

    /**
     * 获取语言模式管理器
     * */
    fun getLocaleManager(): ILocale {
        CsSpManager.name(AppConfigConstants.APP_CONFIG_SP_NAME)
            .put(AppConfigConstants.APP_CONFIG_LOCALE, true)
        AppConfigInit.init()
        return LocaleImpl
    }

    /**
     * 获取暗夜模式管理类
     * */
    fun getUiModeManager(): IUiMode {
        CsSpManager.name(AppConfigConstants.APP_CONFIG_SP_NAME)
            .put(AppConfigConstants.APP_CONFIG_UI_MODE, true)
        AppConfigInit.init()
        return UiModeImpl
    }
}