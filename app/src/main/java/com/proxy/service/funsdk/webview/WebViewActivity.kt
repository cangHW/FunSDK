package com.proxy.service.funsdk.webview

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.core.service.web.CsWeb
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWebViewBinding
import com.proxy.service.funsdk.databinding.WebDialogBinding
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.enums.MixedContentMode
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb
import com.proxy.service.widget.info.dialog.window.CsBaseDialog
import com.proxy.service.widget.info.dialog.window.info.DialogConfig
import com.proxy.service.widget.info.toast.CsToast

/**
 * @author: cangHX
 * @data: 2024/8/3 18:28
 * @desc:
 */
class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, WebViewActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

//    private val url = "file:///android_asset/web/test_bridge.html"
//    private val url = "file:///android_asset/web/test_edittext.html"
    private val url = "https://www.baidu.com"

    private var webView: IWeb? = null

    override fun onCreate(savedInstanceState: Bundle?) {

//        CsBarUtils.setNavigationBarTransparent(this)
//        CsBarUtils.setStatusBarTransparent(this)
        super.onCreate(savedInstanceState)
        enterFullscreen()
    }

    private fun enterFullscreen() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityWebViewBinding {
        return ActivityWebViewBinding.inflate(inflater)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun getWebConfig(): WebConfig {
        val webConfigBuilder = WebConfig.builder()
            .setSavePassword(false)
            .setAllowFileAccess(false)
            .setJavaScriptEnabled(true)
            .setLoadWithOverviewMode(true)
            .setUseWideViewPort(true)
            .setAcceptThirdPartyCookies(true)
            .setMixedContentMode(MixedContentMode.MIXED_CONTENT_ALWAYS_ALLOW)
        return webConfigBuilder.build()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.load_baidu -> {
                binding?.content?.addData("加载", "加载页面")
                binding?.group?.removeAllViews()
                CsWeb.createWebLoader(getWebConfig())
                    ?.setLifecycleOwner(this)
                    ?.loadUrl(url)
                    ?.addJavascriptInterface("xxx", WebBridge())
                    ?.addJavascriptInterface("xxx", WebBridge2())
                    ?.setWebLoadCallback(WebLoadCallbackImpl(binding, "加载"))
                    ?.createTo(binding?.group)
            }

            R.id.create_baidu -> {
                webView = CsWeb.createWebLoader(getWebConfig())
                    ?.setLifecycleOwner(this)
                    ?.loadUrl(url)
                    ?.setWebLoadCallback(WebLoadCallbackImpl(binding, "预加载"))
                    ?.create()
            }

            R.id.show_baidu -> {
                if (webView == null) {
                    CsToast.show("未创建后台加载，无法上屏")
                    return
                }
                binding?.group?.removeAllViews()
                webView?.changeParentView(binding?.group)
                binding?.content?.addData("上屏", "页面上屏")
            }

            R.id.change_group -> {
                if (webView == null) {
                    CsToast.show("未创建后台加载，无法切换位置")
                    return
                }
                binding?.group2?.removeAllViews()
                webView?.changeParentView(binding?.group2)
                binding?.content?.addData("上屏", "切换位置")
            }

            R.id.change_app_window -> {
                if (webView == null) {
                    CsToast.show("未创建后台加载，无法切换应用内window")
                    return
                }
                binding?.content?.addData("上屏", "应用内window")
                DialogImpl(webView).show(this)
            }

            R.id.change_application_window -> {
                if (webView == null) {
                    CsToast.show("未创建后台加载，无法切换全局window")
                    return
                }
                if (!Settings.canDrawOverlays(this)) {
                    CsToast.show("没有权限，无法切换全局window")
                    return
                }
                binding?.content?.addData("上屏", "全局window")
                DialogImpl(webView).show()
            }
        }
    }

    private class WebLoadCallbackImpl(
        private val binding: ActivityWebViewBinding?,
        private val tag: String
    ) : WebLoadCallback {
        override fun onPageError(
            url: String,
            isMainFrameError: Boolean,
            isHttpError: Boolean
        ) {
            super.onPageError(url, isMainFrameError, isHttpError)
            if (isMainFrameError) {
                binding?.content?.addData(tag, "主框架加载出错，页面无法展示")
            } else {
                binding?.content?.addData(tag, "部分内容加载出错 isHttpError = $isHttpError")
            }
        }

        override fun onPageFinished(url: String) {
            super.onPageFinished(url)
            binding?.content?.addData(tag, "加载完成 url = $url")
        }
    }

    private class DialogImpl(
        private val webView: IWeb?
    ) : CsBaseDialog() {

        override fun getDialogConfig(): DialogConfig {
            val config = DialogConfig()
            config.width = ViewGroup. LayoutParams. MATCH_PARENT
            config.height = ViewGroup. LayoutParams. MATCH_PARENT
            config.focusable = true
            return config
        }

        override fun onCreateView(context: Context, parent: ViewGroup): View {
            val binding = WebDialogBinding.inflate(LayoutInflater.from(context), parent, false)
            webView?.changeParentView(binding.webLayout)
            binding.root.setOnClickListener {
                dismiss()
            }
            return binding.root
        }
    }

}