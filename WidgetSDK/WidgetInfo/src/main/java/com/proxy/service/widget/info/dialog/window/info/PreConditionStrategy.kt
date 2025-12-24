package com.proxy.service.widget.info.dialog.window.info

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.dialog.window.base.IDialog
import com.proxy.service.widget.info.dialog.window.constant.DialogConstants

/**
 * 前置策略
 *
 * @author: cangHX
 * @data: 2025/11/27 14:42
 * @desc:
 */
sealed class PreConditionStrategy {

    /**
     * 默认策略，不做任何处理
     */
    object Default : PreConditionStrategy()

    /**
     * 清理当前弹窗前面的全部其他弹窗
     * 触发条件：当前弹窗被添加时
     */
    object ClearAllWhenAdded : PreConditionStrategy() {
        override fun checkStrategyWhenAdded(
            currentDialogs: ArrayList<IDialog>,
            pendingDialogs: ArrayList<IDialog>,
            currentDialog: IDialog
        ) {
            pendingDialogs.forEach {
                dismissDialog(it)
            }
            pendingDialogs.clear()

            currentDialogs.forEach {
                dismissDialog(it)
            }
            currentDialogs.clear()
        }
    }

    /**
     * 清理当前弹窗前面的全部其他弹窗
     * 触发条件：当前弹窗被展示时
     */
    object ClearAllWhenShown : PreConditionStrategy() {
        override fun checkStrategyWhenShown(currentDialogs: ArrayList<IDialog>, currentDialog: IDialog) {
            currentDialogs.forEach {
                dismissDialog(it)
            }
            currentDialogs.clear()
        }
    }

    /**
     * 清理当前弹窗前面的相同优先级弹窗
     * 触发条件：当前弹窗被添加时
     */
    object ClearEqualPriorityWhenAdded : PreConditionStrategy() {
        override fun checkStrategyWhenAdded(
            currentDialogs: ArrayList<IDialog>,
            pendingDialogs: ArrayList<IDialog>,
            currentDialog: IDialog
        ) {
            val waitingIterator = pendingDialogs.iterator()
            while (waitingIterator.hasNext()) {
                val temp = waitingIterator.next()
                if (temp.getDialogPriority() == currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    waitingIterator.remove()
                }
            }

            val showingIterator = currentDialogs.iterator()
            while (showingIterator.hasNext()) {
                val temp = showingIterator.next()
                if (temp.getDialogPriority() == currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    showingIterator.remove()
                }
            }
        }
    }

    /**
     * 清理当前弹窗前面的相同优先级弹窗
     * 触发条件：当前弹窗被展示时
     */
    object ClearEqualPriorityWhenShown : PreConditionStrategy() {
        override fun checkStrategyWhenShown(currentDialogs: ArrayList<IDialog>, currentDialog: IDialog) {
            val showingIterator = currentDialogs.iterator()
            while (showingIterator.hasNext()) {
                val temp = showingIterator.next()
                if (temp.getDialogPriority() == currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    showingIterator.remove()
                }
            }
        }
    }

    /**
     * 清理当前弹窗前面的低优先级和相同优先级弹窗
     * 触发条件：当前弹窗被添加时
     */
    object ClearLowAndEqualPriorityWhenAdded : PreConditionStrategy() {
        override fun checkStrategyWhenAdded(
            currentDialogs: ArrayList<IDialog>,
            pendingDialogs: ArrayList<IDialog>,
            currentDialog: IDialog
        ) {
            val waitingIterator = pendingDialogs.iterator()
            while (waitingIterator.hasNext()) {
                val temp = waitingIterator.next()
                if (temp.getDialogPriority() >= currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    waitingIterator.remove()
                }
            }

            val showingIterator = currentDialogs.iterator()
            while (showingIterator.hasNext()) {
                val temp = showingIterator.next()
                if (temp.getDialogPriority() >= currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    showingIterator.remove()
                }
            }
        }
    }

    /**
     * 清理当前弹窗前面的低优先级和相同优先级弹窗
     * 触发条件：当前弹窗被展示时
     */
    object ClearLowAndEqualPriorityWhenShown : PreConditionStrategy() {
        override fun checkStrategyWhenShown(currentDialogs: ArrayList<IDialog>, currentDialog: IDialog) {
            val showingIterator = currentDialogs.iterator()
            while (showingIterator.hasNext()) {
                val temp = showingIterator.next()
                if (temp.getDialogPriority() >= currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    showingIterator.remove()
                }
            }
        }
    }

    /**
     * 清理当前弹窗前面的低优先级弹窗
     * 触发条件：当前弹窗被添加时
     */
    object ClearLowPriorityWhenAdded : PreConditionStrategy() {
        override fun checkStrategyWhenAdded(
            currentDialogs: ArrayList<IDialog>,
            pendingDialogs: ArrayList<IDialog>,
            currentDialog: IDialog
        ) {
            val waitingIterator = pendingDialogs.iterator()
            while (waitingIterator.hasNext()) {
                val temp = waitingIterator.next()
                if (temp.getDialogPriority() > currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    waitingIterator.remove()
                }
            }

            val showingIterator = currentDialogs.iterator()
            while (showingIterator.hasNext()) {
                val temp = showingIterator.next()
                if (temp.getDialogPriority() > currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    showingIterator.remove()
                }
            }
        }
    }

    /**
     * 清理当前弹窗前面的低优先级弹窗
     * 触发条件：当前弹窗被展示时
     */
    object ClearLowPriorityWhenShown : PreConditionStrategy() {
        override fun checkStrategyWhenShown(currentDialogs: ArrayList<IDialog>, currentDialog: IDialog) {
            val showingIterator = currentDialogs.iterator()
            while (showingIterator.hasNext()) {
                val temp = showingIterator.next()
                if (temp.getDialogPriority() > currentDialog.getDialogPriority()) {
                    dismissDialog(temp)
                    showingIterator.remove()
                }
            }
        }
    }

    /**
     * 自定义策略, 开发者自己选择触发时机以及执行逻辑
     */
    open class Custom : PreConditionStrategy()

    /**
     * 当被添加时检查策略
     * */
    open fun checkStrategyWhenAdded(
        currentDialogs: ArrayList<IDialog>,
        pendingDialogs: ArrayList<IDialog>,
        currentDialog: IDialog
    ) {
    }

    /**
     * 当被展示时检查策略
     * */
    open fun checkStrategyWhenShown(
        currentDialogs: ArrayList<IDialog>,
        currentDialog: IDialog
    ) {
    }

    /**
     * 关闭弹窗
     * */
    protected fun dismissDialog(currentDialog: IDialog) {
        CsLogger.tag(DialogConstants.TAG)
            .i("The strategy is ready to forcibly close the dialog. $currentDialog")
        currentDialog.stop()
        currentDialog.destroy()
    }
}