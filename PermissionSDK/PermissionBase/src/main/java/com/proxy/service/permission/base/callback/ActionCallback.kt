package com.proxy.service.permission.base.callback

/**
 * 权限回调
 *
 * @author: cangHX
 * @data: 2024/11/18 11:35
 * @desc:
 */
interface ActionCallback {

    /**
     * @param list  权限信息
     * */
    fun onAction(list: Array<String>)

}