package com.proxy.service.funsdk.webview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.web.CsWeb
import com.proxy.service.funsdk.R
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb

/**
 * @author: cangHX
 * @data: 2024/8/3 18:28
 * @desc:
 */
class WebViewActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, WebViewActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private var viewGroup: FrameLayout? = null
    private var webView: IWeb? = null

    class XX
    class YY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        viewGroup = findViewById(R.id.content)

        // 测试 Android34 对动态 dex 的限制
        CsWeb.addGlobalJavascriptInterface(XX())
        CsWeb.addGlobalJavascriptInterface(YY())

        webView = CsWeb.createWebLoader(WebConfig.builder().build())
//            ?.setLifecycleOwner(this)
            ?.loadUrl("https://www.baidu.com")
            ?.setWebLoadCallback(object : WebLoadCallback {
                override fun onPageError(
                    url: String,
                    isMainFrameError: Boolean,
                    isHttpError: Boolean
                ) {
                    super.onPageError(url, isMainFrameError, isHttpError)
                    CsLogger.d("onPageError isMainFrameError = $isMainFrameError, isHttpError = $isHttpError")
                }

                override fun onPageFinished(url: String) {
                    super.onPageFinished(url)
                    CsLogger.d("onPageFinished url = $url")
                }
            })
            ?.load()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.load_baidu -> {
                viewGroup?.removeAllViews()
                val web = CsWeb.createWebLoader(WebConfig.builder().build())
                    ?.setLifecycleOwner(this)
                    ?.loadUrl("https://www.baidu.com")
                    ?.setWebLoadCallback(object : WebLoadCallback {
                        override fun onPageError(
                            url: String,
                            isMainFrameError: Boolean,
                            isHttpError: Boolean
                        ) {
                            super.onPageError(url, isMainFrameError, isHttpError)
                            CsLogger.d("onPageError isMainFrameError = $isMainFrameError, isHttpError = $isHttpError")
                        }

                        override fun onPageFinished(url: String) {
                            super.onPageFinished(url)
                            CsLogger.d("onPageFinished url = $url")
                        }
                    })
                    ?.loadTo(viewGroup)
//                web?.setBackgroundColor(Color.TRANSPARENT)
            }

            R.id.show_baidu -> {
                viewGroup?.removeAllViews()
                webView?.changeParentView(viewGroup)
            }
        }
    }

}