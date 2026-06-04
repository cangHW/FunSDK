package com.proxy.service.core.application

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.config.controller.AppConfigInit
import com.proxy.service.core.framework.app.context.ContextInit

/**
 * @author: cangHX
 * @date: 2024/4/28 20:53
 * @desc:
 */
@CloudApiService(serviceTag = "${CoreConfig.SERVICE_TAG_CONFIG_PREFIX}/core_framework")
class CoreFrameworkConfig : CsBaseConfig() {

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return CoreConfig.PRIORITY_CONFIG_CORE_FRAMEWORK
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        ContextInit.init(application)
        AppConfigInit.init()
    }

}