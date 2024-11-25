package com.proxy.service.permission.base.manager.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.callback.DialogDismissCallback

/**
 * @author: cangHX
 * @data: 2024/11/19 18:40
 * @desc:
 */
interface IDialog<T> : IPermissionCallback<T> {

    /**
     * 设置标题
     * */
    fun setTitle(title: String?): T

    /**
     * 设置内容
     * */
    fun setContent(content: String?): T

    /**
     * 设置左侧按钮
     * */
    fun setLeftButton(text: String? = null, click: ButtonClick? = null): T

    /**
     * 设置右侧按钮
     * */
    fun setRightButton(text: String? = null, click: ButtonClick? = null): T

    /**
     * 设置弹窗关闭回调
     * */
    fun setDismissCallback(callback: DialogDismissCallback): T

    /**
     * 展示需要权限的理由
     * */
    fun show(fragment: Fragment)

    /**
     * 展示需要权限的理由
     * */
    fun show(activity: FragmentActivity)

    /**
     * 关闭弹窗
     * */
    fun dismiss()
}