package com.proxy.service.permission.base.manager.base

import com.proxy.service.permission.base.callback.ActionCallback

/**
 * @author: cangHX
 * @data: 2024/11/25 09:49
 * @desc:
 */
interface IShouldShowRequestRationaleCallback<T> {

    /**
     * 拒绝并不再提示的权限回调
     * */
    fun setNoPromptCallback(callback: ActionCallback): T

}