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

    fun onClick(dialog: DialogInterface)

}