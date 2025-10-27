package com.proxy.service.webview.info

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.core.application.base.CsBaseApplication
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.webview.base.constants.WebViewConstants


/**
 * @author: cangHX
 * @data: 2024/8/1 16:37
 * @desc:
 */
@CloudApiService(serviceTag = "application/web")
class WebApplicationImpl : CsBaseApplication() {

    private val webTag = "${WebViewConstants.LOG_TAG_START}Application"

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return -550
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WebView.setDataDirectorySuffix(CsAppUtils.getProcessName())
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