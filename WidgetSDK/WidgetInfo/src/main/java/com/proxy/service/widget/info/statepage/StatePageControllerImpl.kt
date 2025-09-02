package com.proxy.service.widget.info.statepage

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.proxy.service.widget.info.R
import com.proxy.service.widget.info.statepage.config.EmptyPageType
import com.proxy.service.widget.info.statepage.config.ErrorPageType
import com.proxy.service.widget.info.statepage.config.IStatePageController
import com.proxy.service.widget.info.statepage.config.LoadingPageType
import com.proxy.service.widget.info.statepage.empty.EmptyController
import com.proxy.service.widget.info.statepage.error.ErrorController
import com.proxy.service.widget.info.statepage.loading.LoadingController
import com.proxy.service.widget.info.utils.ThreadUtils

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
        ThreadUtils.runOnMainThread {
            hideLoading()
            hideEmpty()
            hideError()
        }
    }

    override fun showLoading(any: Any?) {
        ThreadUtils.runOnMainThread {
            loadingController?.show(any)
            hideEmpty()
            hideError()
        }
    }

    override fun hideLoading() {
        ThreadUtils.runOnMainThread {
            loadingController?.hide()
        }
    }

    override fun showError(
        message: String?,
        buttonTxt: String?,
        any: Any?,
        buttonClick: (() -> Unit)?
    ) {
        ThreadUtils.runOnMainThread {
            errorController?.show(message, buttonTxt, any, buttonClick)
            hideLoading()
            hideEmpty()
        }
    }

    override fun hideError() {
        ThreadUtils.runOnMainThread {
            errorController?.hide()
        }
    }

    override fun showEmpty(message: String?, any: Any?, buttonClick: (() -> Unit)?) {
        ThreadUtils.runOnMainThread {
            emptyController?.show(message, any, buttonClick)
            hideLoading()
            hideError()
        }
    }

    override fun hideEmpty() {
        ThreadUtils.runOnMainThread {
            emptyController?.hide()
        }
    }

}