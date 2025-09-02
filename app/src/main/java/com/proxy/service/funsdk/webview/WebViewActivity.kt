package com.proxy.service.funsdk.webview

import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.proxy.service.core.service.web.CsWeb
import com.proxy.service.funsdk.R
import com.proxy.service.funsdk.base.BaseActivity
import com.proxy.service.funsdk.databinding.ActivityWebViewBinding
import com.proxy.service.webview.base.config.WebConfig
import com.proxy.service.webview.base.listener.WebLoadCallback
import com.proxy.service.webview.base.web.IWeb

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

    private var webView: IWeb? = null

    override fun getViewBinding(inflater: LayoutInflater): ActivityWebViewBinding {
        return ActivityWebViewBinding.inflate(inflater)
    }

    override fun initView() {
        webView = CsWeb.createWebLoader(WebConfig.builder().build())
            ?.setLifecycleOwner(this)
            ?.loadUrl("https://www.baidu.com")
            ?.setWebLoadCallback(object : WebLoadCallback {
                override fun onPageError(
                    url: String,
                    isMainFrameError: Boolean,
                    isHttpError: Boolean
                ) {
                    super.onPageError(url, isMainFrameError, isHttpError)
                    if (isMainFrameError) {
                        binding?.content?.addData(
                            "预加载",
                            "主框架加载出错，无法进行上屏操作"
                        )
                    }else{
                        binding?.content?.addData(
                            "预加载",
                            "部分内容加载出错 isHttpError = $isHttpError"
                        )
                    }
                }

                override fun onPageFinished(url: String) {
                    super.onPageFinished(url)
                    binding?.content?.addData("预加载", "预加载完成, 等待执行上屏操作 url = $url")
                }
            })
            ?.load()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.load_baidu -> {
                binding?.content?.addData("加载", "加载页面")
                binding?.group?.removeAllViews()
                CsWeb.createWebLoader(WebConfig.builder().build())
                    ?.setLifecycleOwner(this)
                    ?.loadUrl("https://www.baidu.com")
                    ?.setWebLoadCallback(object : WebLoadCallback {
                        override fun onPageError(
                            url: String,
                            isMainFrameError: Boolean,
                            isHttpError: Boolean
                        ) {
                            super.onPageError(url, isMainFrameError, isHttpError)
                            if (isMainFrameError) {
                                binding?.content?.addData(
                                    "加载",
                                    "主框架加载出错，页面无法展示"
                                )
                            }else{
                                binding?.content?.addData(
                                    "加载",
                                    "部分内容加载出错 isHttpError = $isHttpError"
                                )
                            }
                        }

                        override fun onPageFinished(url: String) {
                            super.onPageFinished(url)
                            binding?.content?.addData("加载", "加载完成 url = $url")
                        }
                    })
                    ?.loadTo(binding?.group)
            }

            R.id.show_baidu -> {
                binding?.group?.removeAllViews()
                webView?.changeParentView(binding?.group)
                binding?.content?.addData("上屏", "页面上屏")
            }
        }
    }

}