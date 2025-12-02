package com.proxy.service.widget.info.dialog.window.base

import com.proxy.service.widget.info.dialog.window.info.DialogConfig
import com.proxy.service.widget.info.dialog.window.info.PostConditionStrategy
import com.proxy.service.widget.info.dialog.window.info.PreConditionStrategy

/**
 * @author: cangHX
 * @data: 2025/11/27 16:47
 * @desc:
 */
abstract class AbstractConfigDialog : IDialog {

    protected val dialogName: String = this::class.java.simpleName
    protected val tag: String = dialogName

    /**
     * 获取弹窗优先级, 数字越小优先级越高
     * */
    override fun getDialogPriority(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取弹窗配置
     * */
    override fun getDialogConfig(): DialogConfig {
        return DialogConfig()
    }

    /**
     * 获取前置策略
     * */
    override fun getDialogPreConditionStrategy(): PreConditionStrategy {
        return PreConditionStrategy.Default
    }

    /**
     * 获取后置策略
     * */
    override fun getDialogPostConditionStrategy(): PostConditionStrategy {
        return PostConditionStrategy.Default
    }

    /**
     * 弹窗是否应该展示, 可用于设计按条件展示
     * */
    override fun isDialogShouldShow(): Boolean {
        return true
    }

    /**
     * 弹窗是否需要立刻展示
     * */
    override fun isDialogShouldShowImmediate(): Boolean {
        return false
    }

}