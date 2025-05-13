package com.proxy.service.core.application.base

import android.app.Application
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * 用于在初始化之前添加配置, 会在 [CsBaseApplication] 之前执行
 *
 * @author: cangHX
 * @data: 2024/12/4 15:01
 * @desc:
 */
abstract class CsBaseConfig : BaseCoreFw() {

    protected val tag = "${CoreConfig.TAG}Config"

    override fun create(application: Application, isDebug: Boolean) {
        onCreate(application, isDebug)
        CsLogger.tag(tag).d("${this.javaClass.simpleName} onCreate isDebug = $isDebug")
    }
}