package com.proxy.service.widget.info.dialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.constants.WidgetConstants

/**
 * 避免在 activity 不合适的生命周期添加 DialogFragment 导致 crash
 *
 * @author: cangHX
 * @data: 2025/7/8 11:13
 * @desc:
 */
open class CsBaseDialogFragment : DialogFragment() {

    companion object {
        private const val TAG = "${WidgetConstants.TAG}df"
    }

    fun show(manager: FragmentManager) {
        show(manager, null)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (checkActivityCanUse(manager, "show")) {
            try {
                super.show(manager, tag)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable, "DialogFragment show error.")
            }
        }
    }


    fun show(transaction: FragmentTransaction): Int {
        return show(transaction, null)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        return super.show(transaction, tag)
    }

    fun showNow(manager: FragmentManager) {
        showNow(manager, null)
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (checkActivityCanUse(manager, "showNow")) {
            try {
                super.showNow(manager, tag)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable, "DialogFragment showNow error")
            }
        }
    }


    fun showAllowingStateLoss(manager: FragmentManager) {
        showAllowingStateLoss(manager, null)
    }

    fun showAllowingStateLoss(manager: FragmentManager, tag: String?) {
        if (!checkActivityCanUse(manager, "showAllowingStateLoss")) {
            return
        }

        try {
            val dismissedField = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissedField.isAccessible = true
            dismissedField.set(this, false)

            val shownByMeField = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shownByMeField.isAccessible = true
            shownByMeField.set(this, true)

            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG)
                .e(throwable, "DialogFragment showAllowingStateLoss error.")
        }
    }


    fun showNowAllowingStateLoss(manager: FragmentManager) {
        showNowAllowingStateLoss(manager, null)
    }

    fun showNowAllowingStateLoss(manager: FragmentManager, tag: String?) {
        if (!checkActivityCanUse(manager, "showNowAllowingStateLoss")) {
            return
        }

        try {
            val dismissedField = DialogFragment::class.java.getDeclaredField("mDismissed")
            dismissedField.isAccessible = true
            dismissedField.set(this, false)

            val shownByMeField = DialogFragment::class.java.getDeclaredField("mShownByMe")
            shownByMeField.isAccessible = true
            shownByMeField.set(this, true)

            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitNowAllowingStateLoss()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG)
                .e(throwable, "DialogFragment showNowAllowingStateLoss error.")
        }
    }


    override fun dismiss() {
        super.dismiss()
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
    }


    private fun checkActivityCanUse(manager: FragmentManager, funName: String): Boolean {
        val activity = manager.primaryNavigationFragment?.activity
        val isCanUse = activity != null && !activity.isFinishing && !activity.isDestroyed
        if (!isCanUse) {
            CsLogger.tag(TAG)
                .d("The current activity can not be use when $funName. activity = $activity, isFinishing=${activity?.isFinishing}, isDestroyed=${activity?.isDestroyed}")
        }
        return isCanUse
    }
}