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

    private val showingList = ArrayList<IDialog>()
    private val waitingList = ArrayList<IDialog>()

    private var handle: IHandlerOption? = null

    private fun getHandle(): IHandlerOption? {
        if (handle != null) {
            return handle
        }
        handle = CsTask.launchTaskGroup(TASK_NAME)
        return handle
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
            if (showingList.contains(dialog)) {
                CsLogger.tag(DialogConstants.TAG).i("The dialog is showing. $dialog")
                return@start
            }

            if (dialog.isDialogShouldShowImmediate()) {
                CsLogger.tag(DialogConstants.TAG).i("ShowImmediate $dialog")
                dialog.create()
                dialog.getDialogPreConditionStrategy().let {
                    it.checkStrategyWhenAdded(showingList, waitingList, dialog)
                    it.checkStrategyWhenShown(showingList, dialog)
                }
                checkVisibleWhenDialogShow(dialog)
                return@start
            }

            if (!waitingList.contains(dialog)) {
                dialog.create()
                dialog.getDialogPreConditionStrategy()
                    .checkStrategyWhenAdded(showingList, waitingList, dialog)
                waitingList.add(dialog)
            }

            tryShowNextDialog()
        }
    }

    fun requestDismiss(dialog: IDialog) {
        CsLogger.tag(DialogConstants.TAG).i("RequestDismiss $dialog")

        getHandle()?.start {
            if (waitingList.contains(dialog)) {
                dialog.destroy()
                waitingList.remove(dialog)
                return@start
            }

            val index = showingList.indexOf(dialog)
            if (index < 0) {
                tryShowNextDialog()
                return@start
            }

            dialog.stop()
            showingList.remove(dialog)
            tryShowNextDialog()
            dialog.destroy()
        }
    }

    private fun tryShowNextDialog() {
        var selectDialog: IDialog? = null
        for (dialog in waitingList) {
            if (dialog.isDialogShouldShow()) {
                selectDialog = dialog
                break
            }
        }

        // 没有可以展示的弹窗
        if (selectDialog == null) {
            showingList.lastOrNull()?.start()
            return
        }

        var isAllow = true
        showingList.lastOrNull()?.let {
            isAllow = it.getDialogPostConditionStrategy().isAllowDialogShow(it, selectDialog)
        }

        // 不允许展示下一个弹窗
        if (!isAllow) {
            showingList.lastOrNull()?.start()
            return
        }

        CsLogger.tag(DialogConstants.TAG).i("Show next dialog. $selectDialog")

        selectDialog.getDialogPreConditionStrategy()
            .checkStrategyWhenShown(showingList, selectDialog)

        waitingList.remove(selectDialog)
        checkVisibleWhenDialogShow(selectDialog)
    }

    private fun checkVisibleWhenDialogShow(dialog: IDialog) {
        showingList.lastOrNull()?.stop()
        dialog.start()
        showingList.add(dialog)
    }
}