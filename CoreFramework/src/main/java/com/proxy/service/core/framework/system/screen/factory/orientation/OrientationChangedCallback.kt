package com.proxy.service.core.framework.system.screen.factory.orientation

import android.content.res.Configuration

/**
 * @author: cangHX
 * @data: 2026/4/2 10:44
 * @desc:
 */
interface OrientationChangedCallback {
    fun onOrientationChanged(newConfig: Configuration?)
}