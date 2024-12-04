package com.proxy.service.core.application.base

import android.app.Application
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * 基类 Application，用于多 module 初始化
 *
 * @author: cangHX
 * @data: 2024/4/28 18:21
 * @desc:
 */
abstract class CsBaseApplication : BaseCoreFw() {

    protected val tag = "${Constants.TAG}Application"

    final override fun create(application: Application, isDebug: Boolean) {
        CsLogger.tag(tag).d("${this.javaClass.simpleName} onCreate isDebug = $isDebug")
        onCreate(application, isDebug)
    }
}