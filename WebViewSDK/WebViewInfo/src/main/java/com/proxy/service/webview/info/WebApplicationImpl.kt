package com.proxy.service.webview.info

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.webview.info.config.Config


/**
 * @author: cangHX
 * @data: 2024/8/1 16:37
 * @desc:
 */
@CloudApiService(serviceTag = "application/web")
class WebApplicationImpl : CsBaseApplication() {

    private val webTag = "${Config.LOG_TAG_START}Application"

    override fun onCreate(application: Application, isDebug: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WebView.setDataDirectorySuffix("_${CsAppUtils.getProcessName()}")
            if (isDebug) {
                CsTask.startWhenIdle {
                    if (CsAppUtils.isMainProcess()) {
                        CsLogger.tag(webTag).i("web 调试模式开启")
                        WebView.setWebContentsDebuggingEnabled(true)
                    }
                }
            }
        }
    }

}