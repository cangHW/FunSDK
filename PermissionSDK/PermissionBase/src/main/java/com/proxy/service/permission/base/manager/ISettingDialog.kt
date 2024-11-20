package com.proxy.service.permission.base.manager

import com.proxy.service.permission.base.callback.ActionCallback
import com.proxy.service.permission.base.manager.base.IDialog

/**
 * 需要对应权限的理由
 *
 * @author: cangHX
 * @data: 2024/11/18 10:26
 * @desc:
 */
interface ISettingDialog : IDialog<ISettingDialog> {

    /**
     * 允许的权限回调
     * */
    fun setGrantedCallback(callback: ActionCallback): ISettingDialog

    /**
     * 拒绝的权限回调
     * */
    fun setDeniedCallback(callback: ActionCallback): ISettingDialog

}