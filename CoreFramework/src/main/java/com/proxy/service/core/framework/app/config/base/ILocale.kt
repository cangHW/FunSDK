package com.proxy.service.core.framework.app.config.base

import android.content.Context
import java.util.Locale

/**
 * @author: cangHX
 * @data: 2024/12/24 17:57
 * @desc:
 */
interface ILocale : IAction<ILocale> {

    /**
     * 设置语言
     *
     * @param language  语言
     * @param country   国家/地区
     * @param variant   变体, 用于指定特定的方言或版本
     * */
    fun setLocale(context: Context, language: String, country: String = "", variant: String = "")

    /**
     * 设置语言
     * */
    fun setLocale(context: Context, locale: Locale)

    /**
     * 设置跟随系统语言
     * */
    fun setFollowSystemLocale(context: Context)
}