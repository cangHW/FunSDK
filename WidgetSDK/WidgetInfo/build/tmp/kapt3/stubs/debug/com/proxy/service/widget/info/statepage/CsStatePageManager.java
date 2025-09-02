package com.proxy.service.widget.info.statepage;

import java.lang.System;

/**
 * 状态页面管理器（loading、error、empty）
 *
 * @author: cangHX
 * @data: 2025/7/9 20:09
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fJ\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\bJ&\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\t\u001a\u00020\nJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0014J\u0016\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010\u00a8\u0006\u001a"}, d2 = {"Lcom/proxy/service/widget/info/statepage/CsStatePageManager;", "", "()V", "inflate", "Lcom/proxy/service/widget/info/statepage/config/IStatePageController;", "view", "Landroid/view/View;", "emptyPageType", "Lcom/proxy/service/widget/info/statepage/config/EmptyPageType;", "errorPageType", "Lcom/proxy/service/widget/info/statepage/config/ErrorPageType;", "loadingPageType", "Lcom/proxy/service/widget/info/statepage/config/LoadingPageType;", "registerGlobalEmptyPage", "", "key", "", "controller", "Lcom/proxy/service/widget/info/statepage/empty/EmptyController;", "registerGlobalErrorPage", "Lcom/proxy/service/widget/info/statepage/error/ErrorController;", "registerGlobalLoadingPage", "Lcom/proxy/service/widget/info/statepage/loading/LoadingController;", "unregisterGlobalEmptyPage", "unregisterGlobalErrorPage", "unregisterGlobalLoadingPage", "WidgetInfo_debug"})
public final class CsStatePageManager {
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.statepage.CsStatePageManager INSTANCE = null;
    
    private CsStatePageManager() {
        super();
    }
    
    /**
     * 注册全局 loading 页面
     *
     * @param key 唯一标识, 重复则覆盖
     */
    public final void registerGlobalLoadingPage(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.loading.LoadingController controller) {
    }
    
    /**
     * 注册全局 错误 页面
     *
     * @param key 唯一标识, 重复则覆盖
     */
    public final void registerGlobalErrorPage(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.error.ErrorController controller) {
    }
    
    /**
     * 注册全局 空数据 页面
     *
     * @param key 唯一标识, 重复则覆盖
     */
    public final void registerGlobalEmptyPage(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.empty.EmptyController controller) {
    }
    
    /**
     * 移除全局 loading 页面
     */
    public final void unregisterGlobalLoadingPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
    }
    
    /**
     * 移除全局 错误 页面
     */
    public final void unregisterGlobalErrorPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
    }
    
    /**
     * 移除全局 空数据 页面
     */
    public final void unregisterGlobalEmptyPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view  正常视图
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.LoadingPageType loadingPageType) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param emptyPageType     空数据页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.LoadingPageType loadingPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.EmptyPageType emptyPageType) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param errorPageType     错误页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.LoadingPageType loadingPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.ErrorPageType errorPageType) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view          正常视图
     * @param emptyPageType 空数据页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.EmptyPageType emptyPageType) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view          正常视图
     * @param emptyPageType 空数据页面类型
     * @param errorPageType 错误页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.EmptyPageType emptyPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.ErrorPageType errorPageType) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view          正常视图
     * @param errorPageType 错误页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.ErrorPageType errorPageType) {
        return null;
    }
    
    /**
     * 构造一个状态页面控制器
     *
     * @param view              正常视图
     * @param loadingPageType   loading 页面类型
     * @param emptyPageType     空数据页面类型
     * @param errorPageType     错误页面类型
     */
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.statepage.config.IStatePageController inflate(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.LoadingPageType loadingPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.EmptyPageType emptyPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.ErrorPageType errorPageType) {
        return null;
    }
}