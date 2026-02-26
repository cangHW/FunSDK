package com.proxy.service.permission.base.manager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.proxy.service.permission.base.callback.ButtonClick
import com.proxy.service.permission.base.manager.base.IPermissionCallback

/**
 * @author: cangHX
 * @data: 2026/2/10 17:36
 * @desc:
 */
interface IAutoPermissionRequest : IPermissionCallback<IAutoPermissionRequest> {

    /**
     * 设置全部弹窗标题
     * */
    fun setTitle(title: String?): IAutoPermissionRequest

    /**
     * 设置申请权限理由弹窗标题
     * */
    fun setDialogRationaleTitle(title: String?): IAutoPermissionRequest

    /**
     * 设置跳转权限设置弹窗标题
     * */
    fun setDialogSettingTitle(title: String?): IAutoPermissionRequest

    /**
     * 设置内容
     * */
    fun setContent(content: String?): IAutoPermissionRequest

    /**
     * 设置申请权限理由弹窗内容
     * */
    fun setDialogRationaleContent(content: String?): IAutoPermissionRequest

    /**
     * 设置跳转权限设置弹窗内容
     * */
    fun setDialogSettingContent(content: String?): IAutoPermissionRequest

    /**
     * 设置左侧按钮
     * */
    fun setLeftButton(text: String? = null, click: ButtonClick? = null): IAutoPermissionRequest

    /**
     * 设置申请权限理由弹窗左侧按钮
     * */
    fun setDialogRationaleLeftButton(text: String? = null, click: ButtonClick? = null): IAutoPermissionRequest

    /**
     * 设置跳转权限设置弹窗左侧按钮
     * */
    fun setDialogSettingLeftButton(text: String? = null, click: ButtonClick? = null): IAutoPermissionRequest

    /**
     * 设置右侧按钮
     * */
    fun setRightButton(text: String? = null, click: ButtonClick? = null): IAutoPermissionRequest

    /**
     * 设置申请权限理由弹窗右侧按钮
     * */
    fun setDialogRationaleRightButton(text: String? = null, click: ButtonClick? = null): IAutoPermissionRequest

    /**
     * 设置跳转权限设置弹窗右侧按钮
     * */
    fun setDialogSettingRightButton(text: String? = null, click: ButtonClick? = null): IAutoPermissionRequest

    /**
     * 开始申请
     * */
    fun start(activity: FragmentActivity)

    /**
     * 开始申请
     * */
    fun start(fragment: Fragment)
}