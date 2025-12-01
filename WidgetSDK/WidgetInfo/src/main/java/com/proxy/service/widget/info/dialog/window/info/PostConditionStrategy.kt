package com.proxy.service.widget.info.dialog.window.info

import com.proxy.service.widget.info.dialog.window.base.IDialog

/**
 * 后置策略
 *
 * @author: cangHX
 * @data: 2025/11/27 14:45
 * @desc:
 */
sealed class PostConditionStrategy {

    /**
     * 默认, 当前弹窗展示期间允许所有其他弹窗展示
     * */
    object Default : PostConditionStrategy() {
        override fun isAllowDialogShow(preDialog: IDialog, nextDialog: IDialog): Boolean {
            return true
        }
    }

    /**
     * 当前弹窗展示期间不允许任何其他弹窗展示
     * */
    object AllowNone : PostConditionStrategy() {
        override fun isAllowDialogShow(preDialog: IDialog, nextDialog: IDialog): Boolean {
            return false
        }
    }

    /**
     * 当前弹窗展示期间只允许其他更高优先级弹窗展示
     * */
    object AllowHighPriorityOnly : PostConditionStrategy() {
        override fun isAllowDialogShow(preDialog: IDialog, nextDialog: IDialog): Boolean {
            return nextDialog.getDialogPriority() < preDialog.getDialogPriority()
        }
    }

    /**
     * 当前弹窗展示期间只允许其他相同与更高优先级弹窗展示
     * */
    object AllowEqualAndHighPriorityOnly : PostConditionStrategy() {
        override fun isAllowDialogShow(preDialog: IDialog, nextDialog: IDialog): Boolean {
            return nextDialog.getDialogPriority() <= preDialog.getDialogPriority()
        }
    }

    /**
     * 自定义策略, 开发者自己设计当前弹窗展示期间针对其他弹窗的逻辑
     */
    abstract class Custom : PostConditionStrategy()


    /**
     * 是否允许下一个弹窗展示, 返回: true 允许, false 不允许
     * */
    abstract fun isAllowDialogShow(preDialog: IDialog, nextDialog: IDialog): Boolean
}