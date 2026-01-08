package com.proxy.service.widget.info.statepage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.proxy.service.widget.info.statepage.config.EmptyPageType
import com.proxy.service.widget.info.statepage.config.ErrorPageType
import com.proxy.service.widget.info.statepage.config.IStatePageController
import com.proxy.service.widget.info.statepage.config.LoadingPageType
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
     * 注册全局 loading 页面
     *
     * @param key 唯一标识, 重复则覆盖
     * */
    fun registerGlobalLoadingPage(key: String, clazz: Class<out LoadingController>) {
        GlobalPageCache.putLoadingPage(key, clazz)
    }

    /**
     * 注册全局 错误 页面
     *
     * @param key 唯一标识, 重复则覆盖
     * */
    fun registerGlobalErrorPage(key: String, clazz: Class<out ErrorController>) {
        GlobalPageCache.putErrorPage(key, clazz)
    }

    /**
     * 注册全局 空数据 页面
     *
     * @param key 唯一标识, 重复则覆盖
     * */
    fun registerGlobalEmptyPage(key: String, clazz: Class<out EmptyController>) {
        GlobalPageCache.putEmptyPage(key, clazz)
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
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param emptyPageType     空数据页面类型
     * @param errorPageType     错误页面类型
     * */
    fun inflate(
        view: View,
        loadingPageType: LoadingPageType = LoadingPageType.buildDefaultPage(),
        emptyPageType: EmptyPageType = EmptyPageType.buildDefaultPage(),
        errorPageType: ErrorPageType = ErrorPageType.buildDefaultPage()
    ): IStatePageController {
        return StatePageControllerImpl(
            LayoutInflater.from(view.context),
            view,
            0,
            loadingPageType,
            emptyPageType,
            errorPageType
        )
    }

    /**
     * 构造一个状态页面控制器
     *
     * @param context           上下文环境
     * @param resource          正常视图 layout ID
     * @param loadingPageType   loading 页面类型
     * @param emptyPageType     空数据页面类型
     * @param errorPageType     错误页面类型
     * */
    fun inflate(
        context: Context,
        @LayoutRes resource: Int,
        loadingPageType: LoadingPageType = LoadingPageType.buildDefaultPage(),
        emptyPageType: EmptyPageType = EmptyPageType.buildDefaultPage(),
        errorPageType: ErrorPageType = ErrorPageType.buildDefaultPage()
    ): IStatePageController {
        return StatePageControllerImpl(
            LayoutInflater.from(context),
            null,
            resource,
            loadingPageType,
            emptyPageType,
            errorPageType
        )
    }

}