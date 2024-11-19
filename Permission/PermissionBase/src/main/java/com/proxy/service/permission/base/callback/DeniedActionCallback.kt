package com.proxy.service.permission.base.callback

import com.proxy.service.permission.base.manager.IRationaleDialog

/**
 * 权限回调
 *
 * @author: cangHX
 * @data: 2024/11/18 11:35
 * @desc:
 */
interface DeniedActionCallback {

    /**
     * 权限信息
     * */
    fun onAction(list: Array<String>, dialog: IRationaleDialog)

}