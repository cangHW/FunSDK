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

    private val GLOBAL_PAGE_LOADING = ConcurrentHashMap<String, Class<LoadingController>>()
    private val GLOBAL_PAGE_ERROR = ConcurrentHashMap<String, Class<ErrorController>>()
    private val GLOBAL_PAGE_EMPTY = ConcurrentHashMap<String, Class<EmptyController>>()

    /*** *** *** *** *** *** *** *** *** *** Loading *** *** *** *** *** *** *** *** *** ***/

    fun putLoadingPage(key: String, clazz: Class<LoadingController>) {
        GLOBAL_PAGE_LOADING[key] = clazz
    }

    fun getLoadingPage(key: String): LoadingController? {
        val clazz: Class<LoadingController>? = GLOBAL_PAGE_LOADING[key]
        if (clazz != null) {
            return clazz.getDeclaredConstructor().newInstance()
        }
        return getDefaultLoading(key)
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

    fun putErrorPage(key: String, clazz: Class<ErrorController>) {
        GLOBAL_PAGE_ERROR[key] = clazz
    }

    fun getErrorPage(key: String): ErrorController? {
        val clazz: Class<ErrorController>? = GLOBAL_PAGE_ERROR[key]
        if (clazz != null) {
            return clazz.getDeclaredConstructor().newInstance()
        }
        return getDefaultError(key)
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

    fun putEmptyPage(key: String, clazz: Class<EmptyController>) {
        GLOBAL_PAGE_EMPTY[key] = clazz
    }

    fun getEmptyPage(key: String): EmptyController? {
        val clazz: Class<EmptyController>? = GLOBAL_PAGE_EMPTY[key]
        if (clazz != null) {
            return clazz.getDeclaredConstructor().newInstance()
        }
        return getDefaultEmpty(key)
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