package com.proxy.service.widget.info.statepage;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/9 20:10
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u0004\u0018\u00010\u00062\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u0012\u0010\r\u001a\u0004\u0018\u00010\b2\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u0012\u0010\u000e\u001a\u0004\u0018\u00010\n2\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u00062\u0006\u0010\f\u001a\u00020\u0005J\u0010\u0010\u0010\u001a\u0004\u0018\u00010\b2\u0006\u0010\f\u001a\u00020\u0005J\u0010\u0010\u0011\u001a\u0004\u0018\u00010\n2\u0006\u0010\f\u001a\u00020\u0005J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0006J\u0016\u0010\u0015\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\bJ\u0016\u0010\u0016\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\u0005J\u000e\u0010\u0018\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\u0005J\u000e\u0010\u0019\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\u0005R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\b0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/proxy/service/widget/info/statepage/GlobalPageCache;", "", "()V", "GLOBAL_PAGE_EMPTY", "Ljava/util/concurrent/ConcurrentHashMap;", "", "Lcom/proxy/service/widget/info/statepage/empty/EmptyController;", "GLOBAL_PAGE_ERROR", "Lcom/proxy/service/widget/info/statepage/error/ErrorController;", "GLOBAL_PAGE_LOADING", "Lcom/proxy/service/widget/info/statepage/loading/LoadingController;", "getDefaultEmpty", "key", "getDefaultError", "getDefaultLoading", "getEmptyPage", "getErrorPage", "getLoadingPage", "putEmptyPage", "", "controller", "putErrorPage", "putLoadingPage", "removeEmptyPage", "removeErrorPage", "removeLoadingPage", "WidgetInfo_debug"})
public final class GlobalPageCache {
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.statepage.GlobalPageCache INSTANCE = null;
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.proxy.service.widget.info.statepage.loading.LoadingController> GLOBAL_PAGE_LOADING = null;
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.proxy.service.widget.info.statepage.error.ErrorController> GLOBAL_PAGE_ERROR = null;
    private static final java.util.concurrent.ConcurrentHashMap<java.lang.String, com.proxy.service.widget.info.statepage.empty.EmptyController> GLOBAL_PAGE_EMPTY = null;
    
    private GlobalPageCache() {
        super();
    }
    
    /**
     * * *** *** *** *** *** *** *** *** *** Loading *** *** *** *** *** *** *** *** ***
     */
    public final void putLoadingPage(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.loading.LoadingController controller) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.proxy.service.widget.info.statepage.loading.LoadingController getLoadingPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
        return null;
    }
    
    public final void removeLoadingPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
    }
    
    private final com.proxy.service.widget.info.statepage.loading.LoadingController getDefaultLoading(java.lang.String key) {
        return null;
    }
    
    /**
     * * *** *** *** *** *** *** *** *** *** Error *** *** *** *** *** *** *** *** ***
     */
    public final void putErrorPage(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.error.ErrorController controller) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.proxy.service.widget.info.statepage.error.ErrorController getErrorPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
        return null;
    }
    
    public final void removeErrorPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
    }
    
    private final com.proxy.service.widget.info.statepage.error.ErrorController getDefaultError(java.lang.String key) {
        return null;
    }
    
    /**
     * * *** *** *** *** *** *** *** *** *** Empty *** *** *** *** *** *** *** *** ***
     */
    public final void putEmptyPage(@org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.empty.EmptyController controller) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.proxy.service.widget.info.statepage.empty.EmptyController getEmptyPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
        return null;
    }
    
    public final void removeEmptyPage(@org.jetbrains.annotations.NotNull
    java.lang.String key) {
    }
    
    private final com.proxy.service.widget.info.statepage.empty.EmptyController getDefaultEmpty(java.lang.String key) {
        return null;
    }
}