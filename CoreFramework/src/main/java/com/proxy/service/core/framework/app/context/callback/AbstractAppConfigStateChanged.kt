package com.proxy.service.core.framework.app.context.callback

import android.content.res.Configuration

/**
 * @author: cangHX
 * @data: 2025/9/15 15:44
 * @desc:
 */
abstract class AbstractAppConfigStateChanged {

    /**
     * 应用配置变化
     * */
    open fun onConfigurationChanged(newConfig: Configuration) {

    }

    /**
     * 应用内存不足
     * */
    open fun onLowMemory() {

    }

}