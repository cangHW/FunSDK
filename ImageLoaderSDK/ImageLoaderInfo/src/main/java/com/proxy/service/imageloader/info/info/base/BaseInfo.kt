package com.proxy.service.imageloader.info.info.base

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * @author: cangHX
 * @data: 2024/6/4 16:32
 * @desc:
 */
open class BaseInfo {

    var activity: FragmentActivity? = null
    var fragment: Fragment? = null
    var view: View? = null
    var ctx: Context? = null

    open fun <T : BaseInfo> copy(info: T) {
        activity = info.activity
        fragment = info.fragment
        view = info.view
        ctx = info.ctx
    }
}