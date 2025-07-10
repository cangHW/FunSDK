package com.proxy.service.widget.info.statepage

import android.view.View
import com.proxy.service.widget.info.statepage.config.EmptyPageType
import com.proxy.service.widget.info.statepage.config.ErrorPageType
import com.proxy.service.widget.info.statepage.config.IStatePageController
import com.proxy.service.widget.info.statepage.config.LoadingPageType
import com.proxy.service.widget.info.statepage.config.PageConfig
import com.proxy.service.widget.info.statepage.empty.EmptyController
import com.proxy.service.widget.info.statepage.error.ErrorController
import com.proxy.service.widget.info.statepage.loading.LoadingController

/**
 * 状态页面管理器（loading、error、empty）
 *
 * @author: cangHX
 * @data: 2025/7/9 20:09
 * @desc:
 */
object CsStatePageManager {

    /**
     * 设置 loading 页面背景色, 默认颜色 [PageConfig.background_loading]
     * */
    fun setBackgroundColorForLoading(color: String) {
        PageConfig.background_loading = color
    }

    /**
     * 设置 空数据 页面背景色, 默认颜色 [PageConfig.background_empty]
     * */
    fun setBackgroundColorForEmpty(color: String) {
        PageConfig.background_empty = color
    }

    /**
     * 设置 错误 页面背景色, 默认颜色 [PageConfig.background_error]
     * */
    fun setBackgroundColorForError(color: String) {
        PageConfig.background_error = color
    }

    /**
     * 注册全局 loading 页面
     * */
    fun registerGlobalLoadingPage(key: String, controller: LoadingController) {
        GlobalPageCache.putLoadingPage(key, controller)
    }

    /**
     * 注册全局 错误 页面
     * */
    fun registerGlobalErrorPage(key: String, controller: ErrorController) {
        GlobalPageCache.putErrorPage(key, controller)
    }

    /**
     * 注册全局 空数据 页面
     * */
    fun registerGlobalEmptyPage(key: String, controller: EmptyController) {
        GlobalPageCache.putEmptyPage(key, controller)
    }

    /**
     * 移除全局 loading 页面
     * */
    fun unregisterGlobalLoadingPage(key: String) {
        GlobalPageCache.removeLoadingPage(key)
    }

    /**
     * 移除全局 错误 页面
     * */
    fun unregisterGlobalErrorPage(key: String) {
        GlobalPageCache.removeErrorPage(key)
    }

    /**
     * 移除全局 空数据 页面
     * */
    fun unregisterGlobalEmptyPage(key: String) {
        GlobalPageCache.removeEmptyPage(key)
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view  正常视图
     * */
    fun inflate(view: View): IStatePageController {
        return inflate(
            view,
            LoadingPageType.buildDefaultPage(),
            EmptyPageType.buildDefaultPage(),
            ErrorPageType.buildDefaultPage()
        )
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * */
    fun inflate(view: View, loadingPageType: LoadingPageType): IStatePageController {
        return inflate(
            view,
            loadingPageType,
            EmptyPageType.buildDefaultPage(),
            ErrorPageType.buildDefaultPage()
        )
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param emptyPageType     空数据页面类型
     * */
    fun inflate(
        view: View,
        loadingPageType: LoadingPageType,
        emptyPageType: EmptyPageType
    ): IStatePageController {
        return inflate(view, loadingPageType, emptyPageType, ErrorPageType.buildDefaultPage())
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param errorPageType     错误页面类型
     * */
    fun inflate(
        view: View,
        loadingPageType: LoadingPageType,
        errorPageType: ErrorPageType
    ): IStatePageController {
        return inflate(view, loadingPageType, EmptyPageType.buildDefaultPage(), errorPageType)
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view          正常视图
     * @param emptyPageType 空数据页面类型
     * */
    fun inflate(view: View, emptyPageType: EmptyPageType): IStatePageController {
        return inflate(
            view,
            LoadingPageType.buildDefaultPage(),
            emptyPageType,
            ErrorPageType.buildDefaultPage()
        )
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view          正常视图
     * @param emptyPageType 空数据页面类型
     * @param errorPageType 错误页面类型
     * */
    fun inflate(
        view: View,
        emptyPageType: EmptyPageType,
        errorPageType: ErrorPageType
    ): IStatePageController {
        return inflate(view, LoadingPageType.buildDefaultPage(), emptyPageType, errorPageType)
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view          正常视图
     * @param errorPageType 错误页面类型
     * */
    fun inflate(view: View, errorPageType: ErrorPageType): IStatePageController {
        return inflate(
            view,
            LoadingPageType.buildDefaultPage(),
            EmptyPageType.buildDefaultPage(),
            errorPageType
        )
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param emptyPageType     空数据页面类型
     * @param errorPageType     错误页面类型
     * */
    fun inflate(
        view: View,
        loadingPageType: LoadingPageType,
        emptyPageType: EmptyPageType,
        errorPageType: ErrorPageType
    ): IStatePageController {
        return StatePageControllerImpl(view, loadingPageType, emptyPageType, errorPageType)
    }

}