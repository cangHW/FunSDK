package com.proxy.service.core.framework.app.config.base

import android.content.Context
import android.content.res.Configuration
import com.proxy.service.core.framework.app.config.locale.LocaleCheck
import com.proxy.service.core.framework.app.config.uimode.UiModeCheck

/**
 * @author: cangHX
 * @data: 2024/12/24 18:44
 * @desc:
 */
abstract class BaseConfigurationCheck {

    companion object {
        fun getAllConfigurations(): ArrayList<BaseConfigurationCheck> {
            val list = ArrayList<BaseConfigurationCheck>()
            list.add(LocaleCheck)
            list.add(UiModeCheck)
            return list
        }
    }

    /**
     * 系统配置更新
     * */
    abstract fun onSystemConfigurationChanged(newConfig: Configuration)

    /**
     * 检查配置
     *
     * @return 是否有更新
     * */
    abstract fun onConfigurationCheck(context: Context, config: Configuration): Boolean
}