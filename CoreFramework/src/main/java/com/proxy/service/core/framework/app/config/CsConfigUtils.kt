package com.proxy.service.core.framework.app.config

import com.proxy.service.core.framework.app.config.base.ILocale
import com.proxy.service.core.framework.app.config.base.IUiMode
import com.proxy.service.core.framework.app.config.locale.LocaleImpl
import com.proxy.service.core.framework.app.config.uimode.UiModeImpl

/**
 * 应用配置工具类
 *
 * @author: cangHX
 * @data: 2024/12/24 17:45
 * @desc:
 */
object CsConfigUtils {

    /**
     * 获取语言模式管理器
     * */
    fun getLocaleManager(): ILocale {
        return LocaleImpl
    }

    /**
     * 获取暗夜模式管理类
     * */
    fun getUiModeManager(): IUiMode {
        return UiModeImpl
    }
}