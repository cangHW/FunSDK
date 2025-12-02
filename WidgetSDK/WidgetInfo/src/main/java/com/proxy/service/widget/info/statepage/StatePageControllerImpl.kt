package com.proxy.service.widget.info.statepage

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.widget.info.R
import com.proxy.service.widget.info.statepage.config.EmptyPageType
import com.proxy.service.widget.info.statepage.config.ErrorPageType
import com.proxy.service.widget.info.statepage.config.IStatePageController
import com.proxy.service.widget.info.statepage.config.LoadingPageType
import com.proxy.service.widget.info.statepage.empty.EmptyController
import com.proxy.service.widget.info.statepage.error.ErrorController
import com.proxy.service.widget.info.statepage.loading.LoadingController

/**
 * @author: cangHX
 * @data: 2025/7/9 20:21
 * @desc:
 */
class StatePageControllerImpl(
    view: View,
    loadingPageType: LoadingPageType,
    emptyPageType: EmptyPageType,
    errorPageType: ErrorPageType
) : IStatePageController {

    private val rootView: FrameLayout = LayoutInflater.from(view.context)
        .inflate(R.layout.cs_widget_state_page_basic, null, false) as FrameLayout

    private var loadingController: LoadingController? = null
    private var emptyController: EmptyController? = null
    private var errorController: ErrorController? = null

    init {
        loadingController = GlobalPageCache.getLoadingPage(loadingPageType.key)
        emptyController = GlobalPageCache.getEmptyPage(emptyPageType.key)
        errorController = GlobalPageCache.getErrorPage(errorPageType.key)

        rootView.addView(view)
        loadingController?.initView(rootView)
        emptyController?.initView(rootView)
        errorController?.initView(rootView)
    }

    override fun getRootView(): View {
        return rootView
    }

    override fun showSuccess() {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                hideLoading()
                hideEmpty()
                hideError()
                return ""
            }
        })?.start()
    }

    override fun showLoading(any: Any?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                loadingController?.show(any)
                hideEmpty()
                hideError()
                return ""
            }
        })?.start()
    }

    override fun hideLoading() {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                loadingController?.hide()
                return ""
            }
        })?.start()
    }

    override fun showError(
        message: String?,
        buttonTxt: String?,
        any: Any?,
        buttonClick: (() -> Unit)?
    ) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                errorController?.show(message, buttonTxt, any, buttonClick)
                hideLoading()
                hideEmpty()
                return ""
            }
        })?.start()
    }

    override fun hideError() {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                errorController?.hide()
                return ""
            }
        })?.start()
    }

    override fun showEmpty(message: String?, any: Any?, buttonClick: (() -> Unit)?) {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                emptyController?.show(message, any, buttonClick)
                hideLoading()
                hideError()
                return ""
            }
        })?.start()
    }

    override fun hideEmpty() {
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                emptyController?.hide()
                return ""
            }
        })?.start()
    }

}