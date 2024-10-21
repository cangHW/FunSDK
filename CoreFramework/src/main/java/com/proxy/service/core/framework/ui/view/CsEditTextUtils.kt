package com.proxy.service.core.framework.ui.view

import android.content.Context
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * view，输入框相关工具
 *
 * @author: cangHX
 * @data: 2024/9/20 17:33
 * @desc:
 */
object CsEditTextUtils {

    private const val TAG = "${Constants.TAG}EditText"

    /**
     * 弹出键盘
     * */
    fun showSoftInput(editText: EditText) {
        editText.requestFocus()
        val imm =
            editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.showSoftInput(editText, 0)
    }

    /**
     * 隐藏键盘
     * */
    fun hideSoftInput(view: View? = null) {
        var tempView = view
        if (tempView is EditText) {
            tempView = getOtherView(tempView)
        }

        if (tempView == null) {
            val activity = CsContextManager.getTopActivity() ?: return
            val window = activity.window ?: return
            tempView = window.decorView
        }

        try {
            tempView.requestFocus()
            val imm =
                tempView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
            imm?.hideSoftInputFromWindow(tempView.windowToken, 0)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).d(throwable)
        }
    }

    /**
     * 密文模式
     * */
    fun hideInputContent(editText: EditText) {
        editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        editText.setSelection(editText.text.length)
    }

    /**
     * 明文模式
     * */
    fun showInputContent(editText: EditText) {
        editText.transformationMethod = PasswordTransformationMethod.getInstance()
        editText.setSelection(editText.text.length)
    }

    private fun getOtherView(view: View): View? {
        var tempView = view
        while (tempView.parent is ViewGroup) {
            val viewGroup = tempView.parent as ViewGroup
            for (i in 0 until viewGroup.childCount) {
                val currentView = viewGroup.getChildAt(i)
                val flag = currentView is EditText
                if (!flag) {
                    return currentView
                }
            }
            tempView = viewGroup
        }
        return null
    }
}