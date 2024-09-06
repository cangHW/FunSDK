package com.proxy.service.core.application.base

import android.app.Application
import androidx.annotation.CallSuper
import com.proxy.service.base.BaseService
import com.proxy.service.core.framework.log.CsLogger

/**
 * @author: cangHX
 * @data: 2024/4/28 18:21
 * @desc:
 */
abstract class CsBaseApplication : BaseService {

    /**
     * 执行优先级，数字越小的优先执行[0 - ]
     * */
    open fun priority(): Int {
        return 0
    }

    @CallSuper
    open fun onCreate(application: Application, isDebug: Boolean) {
        CsLogger.tag(this.javaClass.simpleName).d("onCreate isDebug = $isDebug")
    }

}