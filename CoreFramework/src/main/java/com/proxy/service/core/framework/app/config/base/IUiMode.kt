package com.proxy.service.core.framework.app.config.base

import android.content.Context

/**
 * @author: cangHX
 * @data: 2024/12/26 10:03
 * @desc:
 */
interface IUiMode : IAction<IUiMode> {

    /**
     * 设置跟随系统模式
     * */
    fun setFollowSystemMode(context: Context)

    /**
     * 开启暗夜模式
     * */
    fun openNightMode(context: Context)

    /**
     * 关闭暗夜模式
     * */
    fun closeNightMode(context: Context)

}