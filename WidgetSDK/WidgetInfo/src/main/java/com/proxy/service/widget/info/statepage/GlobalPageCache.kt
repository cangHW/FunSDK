package com.proxy.service.widget.info.statepage

import com.proxy.service.widget.info.statepage.config.EmptyPageType
import com.proxy.service.widget.info.statepage.config.ErrorPageType
import com.proxy.service.widget.info.statepage.config.LoadingPageType
import com.proxy.service.widget.info.statepage.empty.EmptyController
import com.proxy.service.widget.info.statepage.empty.impl.WithOutRefreshEmpty
import com.proxy.service.widget.info.statepage.error.ErrorController
import com.proxy.service.widget.info.statepage.error.impl.WithRefreshError
import com.proxy.service.widget.info.statepage.loading.LoadingController
import com.proxy.service.widget.info.statepage.loading.impl.RotationLoading
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: cangHX
 * @data: 2025/7/9 20:10
 * @desc:
 */
object GlobalPageCache {

    private val GLOBAL_PAGE_LOADING: ConcurrentHashMap<String, LoadingController> =
        ConcurrentHashMap<String, LoadingController>()

    private val GLOBAL_PAGE_ERROR: ConcurrentHashMap<String, ErrorController> =
        ConcurrentHashMap<String, ErrorController>()

    private val GLOBAL_PAGE_EMPTY: ConcurrentHashMap<String, EmptyController> =
        ConcurrentHashMap<String, EmptyController>()

    /*** *** *** *** *** *** *** *** *** *** Loading *** *** *** *** *** *** *** *** *** ***/

    fun putLoadingPage(key: String, controller: LoadingController) {
        GLOBAL_PAGE_LOADING[key] = controller
    }

    fun getLoadingPage(key: String): LoadingController? {
        var controller: LoadingController? = GLOBAL_PAGE_LOADING[key]
        if (controller == null) {
            controller = getDefaultLoading(key)
        }
        return controller
    }

    fun removeLoadingPage(key: String) {
        GLOBAL_PAGE_LOADING.remove(key)
    }

    private fun getDefaultLoading(key: String): LoadingController? {
        if (key == LoadingPageType.ROTATION) {
            return RotationLoading()
        }
        return null
    }

    /*** *** *** *** *** *** *** *** *** *** Error *** *** *** *** *** *** *** *** *** ***/

    fun putErrorPage(key: String, controller: ErrorController) {
        GLOBAL_PAGE_ERROR[key] = controller
    }

    fun getErrorPage(key: String): ErrorController? {
        var controller: ErrorController? = GLOBAL_PAGE_ERROR[key]
        if (controller == null) {
            controller = getDefaultError(key)
        }
        return controller
    }

    fun removeErrorPage(key: String) {
        GLOBAL_PAGE_ERROR.remove(key)
    }

    private fun getDefaultError(key: String): ErrorController? {
        if (key == ErrorPageType.WITH_REFRESH) {
            return WithRefreshError()
        }
        return null
    }

    /*** *** *** *** *** *** *** *** *** *** Empty *** *** *** *** *** *** *** *** *** ***/

    fun putEmptyPage(key: String, controller: EmptyController) {
        GLOBAL_PAGE_EMPTY[key] = controller
    }

    fun getEmptyPage(key: String): EmptyController? {
        var controller: EmptyController? = GLOBAL_PAGE_EMPTY[key]
        if (controller == null) {
            controller = getDefaultEmpty(key)
        }
        return controller
    }

    fun removeEmptyPage(key: String) {
        GLOBAL_PAGE_EMPTY.remove(key)
    }

    private fun getDefaultEmpty(key: String): EmptyController? {
        if (key == EmptyPageType.WITH_OUT_REFRESH) {
            return WithOutRefreshEmpty()
        }
        return null
    }

}