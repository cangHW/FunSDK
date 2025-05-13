package com.proxy.service.core.framework.app.config.controller

import androidx.core.app.ActivityCompat
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2024/12/26 10:19
 * @desc:
 */
object RefreshManager {

    private const val TAG = "${CoreConfig.TAG}ConfigRefresh"

    fun refreshNow() {
        CsContextManager.getAllActivity().forEach {
            try {
                ActivityCompat.recreate(it)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

}