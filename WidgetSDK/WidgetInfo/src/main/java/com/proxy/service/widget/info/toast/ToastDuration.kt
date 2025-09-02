package com.proxy.service.widget.info.toast

import android.widget.Toast

/**
 * @author: cangHX
 * @data: 2025/7/8 15:51
 * @desc:
 */
enum class ToastDuration(val value: Int) {

    /**
     * 短时间
     * */
    LENGTH_SHORT(Toast.LENGTH_SHORT),

    /**
     * 长时间
     * */
    LENGTH_LONG(Toast.LENGTH_LONG);

}