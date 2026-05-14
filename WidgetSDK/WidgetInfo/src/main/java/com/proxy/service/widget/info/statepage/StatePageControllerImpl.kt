package com.proxy.service.widget.info.statepage

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.core.utils.ThreadUtils
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
 * @date: 2025/7/9 20:21
 * @desc:
 */
class StatePageControllerImpl(
    inflater: LayoutInflater,
    view: View?,
    resource: Int,
    loadingPageType: LoadingPageType,
    emptyPageType: EmptyPageType,
    errorPageType: ErrorPageType
) : IStatePageController {

    private val rootView: FrameLayout = inflater.inflate(
        R.layout.cs_widget_state_page_basic,
        null,
        false
    ) as FrameLayout

    private var contentView: View? = null

    private var loadingController: LoadingController? = null
    private var emptyController: EmptyController? = null
    private var errorController: ErrorController? = null

    init {
        emptyController = GlobalPageCache.getEmptyPage(emptyPageType.key)
        errorController = GlobalPageCache.getErrorPage(errorPageType.key)
        loadingController = GlobalPageCache.getLoadingPage(loadingPageType.key)

        if (view != null) {
            contentView = view
            rootView.addView(contentView)
        } else if (resource != 0) {
            contentView = inflater.inflate(resource, rootView, true)
        }
        emptyController?.initView(rootView)
        errorController?.initView(rootView)
        loadingController?.initView(rootView)
    }

    override fun getRootView(): View {
        return rootView
    }

    override fun showSuccess() {
        ThreadUtils.runUiThread {
            contentView?.visibility = View.VISIBLE
            hideLoading()
            hideEmpty()
            hideError()
        }
    }

    override fun hideContent() {
        ThreadUtils.runUiThread {
            contentView?.visibility = View.INVISIBLE
        }
    }

    override fun showLoadingOnly(any: Any?) {
        ThreadUtils.runUiThread {
            loadingController?.show(any)
        }
    }

    override fun showLoading(any: Any?) {
        ThreadUtils.runUiThread {
            loadingController?.show(any)
            hideEmpty()
            hideError()
        }
    }

    override fun hideLoading() {
        ThreadUtils.runUiThread {
            loadingController?.hide()
        }
    }

    override fun showError(
        message: String?,
        buttonTxt: String?,
        any: Any?,
        buttonClick: ((any: Any?) -> Unit)?
    ) {
        ThreadUtils.runUiThread {
            errorController?.show(message, buttonTxt, any, buttonClick)
            hideLoading()
            hideEmpty()
        }
    }

    override fun hideError() {
        ThreadUtils.runUiThread {
            errorController?.hide()
        }
    }

    override fun showEmpty(message: String?, any: Any?, buttonClick: ((any: Any?) -> Unit)?) {
        ThreadUtils.runUiThread {
            emptyController?.show(message, any, buttonClick)
            hideLoading()
            hideError()
        }
    }

    override fun hideEmpty() {
        ThreadUtils.runUiThread {
            emptyController?.hide()
        }
    }

}