package com.proxy.service.core.framework.app.config.locale.compat.controller

import android.content.res.Configuration
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2025/3/20 14:19
 * @desc:
 */
interface LocaleController {

    /**
     * 获取当前 local
     * */
    fun getCurrentLocal(config: Configuration): Locale

    /**
     * 设置 local
     * */
    fun setCurrentLocal(config: Configuration, locale: Locale)

}