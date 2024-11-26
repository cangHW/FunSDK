package com.proxy.service.permission.base.callback

/**
 * @author: cangHX
 * @data: 2024/11/19 10:18
 * @desc:
 */
interface ButtonClick {

    interface DialogInterface {
        /**
         * 关闭 dialog
         * */
        fun dismiss()
    }

    /**
     * 按钮点击回调
     *
     * @param dialog    dialog 管理接口, 可用于关闭 dialog
     *
     * @return 是否接管当前点击事件, true 接管
     * */
    fun onClick(dialog: DialogInterface): Boolean

}