package com.proxy.service.widget.info.dialog.window.base

import com.proxy.service.widget.info.dialog.window.info.DialogConfig
import com.proxy.service.widget.info.dialog.window.info.PostConditionStrategy
import com.proxy.service.widget.info.dialog.window.info.PreConditionStrategy

/**
 * 弹窗基础
 *
 * @author: cangHX
 * @data: 2025/11/27 10:57
 * @desc:
 */
interface IDialog {

    /**
     * 获取弹窗优先级
     * */
    fun getDialogPriority(): Int

    /**
     * 获取弹窗配置
     * */
    fun getDialogConfig(): DialogConfig

    /**
     * 获取前置策略
     * */
    fun getDialogPreConditionStrategy(): PreConditionStrategy

    /**
     * 获取后置策略
     * */
    fun getDialogPostConditionStrategy(): PostConditionStrategy

    /**
     * 弹窗是否应该展示, 可用于设计按条件展示
     * */
    fun isDialogShouldShow(): Boolean

    /**
     * 弹窗是否需要立刻展示
     * */
    fun isDialogShouldShowImmediate(): Boolean


    /**
     * 创建
     * */
    fun create()

    /**
     * 开始
     * */
    fun start()

    /**
     * 停止
     * */
    fun stop()

    /**
     * 销毁
     * */
    fun destroy()

}