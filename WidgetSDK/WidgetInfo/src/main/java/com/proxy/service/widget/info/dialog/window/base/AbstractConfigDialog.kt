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

    override fun getDialogPriority(): Int {
        return 0
    }

    override fun getDialogConfig(): DialogConfig {
        return DialogConfig()
    }

    override fun getDialogPreConditionStrategy(): PreConditionStrategy {
        return PreConditionStrategy.Default
    }

    override fun getDialogPostConditionStrategy(): PostConditionStrategy {
        return PostConditionStrategy.Default
    }

    override fun isDialogShouldShow(): Boolean {
        return true
    }

    override fun isDialogShouldShowImmediate(): Boolean {
        return false
    }

}