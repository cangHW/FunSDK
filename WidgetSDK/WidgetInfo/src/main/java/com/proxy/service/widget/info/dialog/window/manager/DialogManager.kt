package com.proxy.service.widget.info.dialog.window.manager

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import com.proxy.service.widget.info.dialog.window.base.IDialog
import com.proxy.service.widget.info.dialog.window.constant.DialogConstants
import java.lang.StringBuilder

/**
 * @author: cangHX
 * @data: 2025/11/27 11:27
 * @desc:
 */
object DialogManager {

    private const val TASK_NAME = "cs-dialog-loop"

    private val currentDialogs = ArrayList<IDialog>()
    private val pendingDialogs = ArrayList<IDialog>()

    private var handle: IHandlerOption? = null

    private fun getHandle(): IHandlerOption? {
        if (handle != null) {
            return handle
        }
        handle = CsTask.launchTaskGroup(TASK_NAME)
        return handle
    }

    fun getCurrentDialogs(): ArrayList<IDialog> {
        return ArrayList(currentDialogs)
    }

    fun getPendingDialogs(): ArrayList<IDialog> {
        return ArrayList(pendingDialogs)
    }

    fun requestShow(dialog: IDialog) {
        val builder = StringBuilder()
        builder.append(dialog.toString()).append("\n")
        builder.append("isDialogShouldShowImmediate=")
            .append(dialog.isDialogShouldShowImmediate())
            .append("\n")
        builder.append("PreConditionStrategy=")
            .append(dialog.getDialogPreConditionStrategy().javaClass.simpleName)
            .append("\n")
        builder.append("PostConditionStrategy=")
            .append(dialog.getDialogPostConditionStrategy().javaClass.simpleName)
            .append("\n")
        builder.append("config=").append(dialog.getDialogConfig()).append("\n")
        CsLogger.tag(DialogConstants.TAG).i("RequestShow $builder")

        getHandle()?.start {
            if (currentDialogs.contains(dialog)) {
                CsLogger.tag(DialogConstants.TAG).i("The dialog is showing. $dialog")
                return@start
            }

            if (dialog.isDialogShouldShowImmediate()) {
                CsLogger.tag(DialogConstants.TAG).i("ShowImmediate $dialog")
                dialog.create()
                dialog.getDialogPreConditionStrategy().let {
                    it.checkStrategyWhenAdded(currentDialogs, pendingDialogs, dialog)
                    it.checkStrategyWhenShown(currentDialogs, dialog)
                }
                currentDialogs.lastOrNull()?.stop()
                dialog.start()
                currentDialogs.add(dialog)
                return@start
            }

            if (!pendingDialogs.contains(dialog)) {
                dialog.create()
                dialog.getDialogPreConditionStrategy()
                    .checkStrategyWhenAdded(currentDialogs, pendingDialogs, dialog)
                pendingDialogs.add(dialog)
            }

            tryShowNextDialog()
        }
    }

    fun requestDismiss(dialog: IDialog) {
        CsLogger.tag(DialogConstants.TAG).i("RequestDismiss $dialog")

        getHandle()?.start {
            if (pendingDialogs.contains(dialog)) {
                dialog.destroy()
                pendingDialogs.remove(dialog)
                return@start
            }

            val index = currentDialogs.indexOf(dialog)
            if (index < 0) {
                tryShowNextDialog()
                return@start
            }

            dialog.stop()
            currentDialogs.remove(dialog)
            tryShowNextDialog()
            dialog.destroy()
        }
    }

    private fun tryShowNextDialog() {
        for (dialog in pendingDialogs) {
            if (!dialog.isDialogShouldShow()) {
                continue
            }

            var isAllow = true
            val lastShowDialog = currentDialogs.lastOrNull()
            lastShowDialog?.let {
                isAllow = it.getDialogPostConditionStrategy().isNextDialogAllowed(
                    currentDialogs, it,
                    pendingDialogs, dialog
                )
            }

            // 该弹窗不被允许展示
            if (!isAllow) {
                continue
            }

            CsLogger.tag(DialogConstants.TAG).i("Show next dialog. $dialog")

            dialog.getDialogPreConditionStrategy()
                .checkStrategyWhenShown(currentDialogs, dialog)

            pendingDialogs.remove(dialog)
            lastShowDialog?.stop()
            currentDialogs.add(dialog)
        }

        currentDialogs.lastOrNull()?.start()
    }
}