package com.proxy.service.widget.info.toast.info

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * @author: cangHX
 * @data: 2025/9/24 09:39
 * @desc:
 */
open class BaseViewInfo(val rootView: View) {

    /**
     * 更新 icon
     * */
    open fun updateIcon(bitmap: Bitmap?) {}

    /**
     * 更新 icon
     * */
    open fun updateIcon(drawable: Drawable?) {}

    /**
     * 更新 icon
     * */
    open fun updateIcon(@DrawableRes resId: Int) {}


    /**
     * 更新 txt
     * */
    open fun updateTxt(content: String) {}

    /**
     * 更新 txt
     * */
    open fun updateTxt(@StringRes resId: Int) {}

}