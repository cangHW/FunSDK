package com.proxy.service.widget.info.toast;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/8 14:31
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u000eJ\u0018\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0007\u001a\u00020\bJ.\u0010\u0016\u001a\u00020\u000e2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\b\u0001\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ,\u0010\u0016\u001a\u00020\u000e2\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u001e\u001a\u00020\u000b2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ.\u0010\u0016\u001a\u00020\u000e2\b\u0010\u0017\u001a\u0004\u0018\u00010\u001f2\b\b\u0001\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ,\u0010\u0016\u001a\u00020\u000e2\b\u0010\u0017\u001a\u0004\u0018\u00010\u001f2\u0006\u0010\u001e\u001a\u00020\u000b2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ$\u0010\u0016\u001a\u00020\u000e2\b\b\u0001\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ.\u0010\u0016\u001a\u00020\u000e2\b\b\u0001\u0010 \u001a\u00020\u001a2\b\b\u0001\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ,\u0010\u0016\u001a\u00020\u000e2\b\b\u0001\u0010 \u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u000b2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bJ\"\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u001e\u001a\u00020\u000b2\b\b\u0002\u0010\u001b\u001a\u00020\u001c2\b\b\u0002\u0010\u001d\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/proxy/service/widget/info/toast/CsToast;", "", "()V", "config", "Lcom/proxy/service/widget/info/toast/ToastConfig;", "currentToast", "Landroid/widget/Toast;", "factory", "Lcom/proxy/service/widget/info/toast/ToastViewFactory;", "toastMap", "Ljava/util/HashMap;", "", "Lcom/proxy/service/widget/info/toast/ToastViewFactory$BaseViewInfo;", "clearToastCache", "", "createToast", "context", "Landroid/content/Context;", "view", "Landroid/view/View;", "setGlobalToastConfig", "setGlobalToastViewFactory", "show", "icon", "Landroid/graphics/Bitmap;", "stringId", "", "duration", "Lcom/proxy/service/widget/info/toast/ToastDuration;", "tag", "content", "Landroid/graphics/drawable/Drawable;", "resId", "WidgetInfo_release"})
public final class CsToast {
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.toast.CsToast INSTANCE = null;
    private static final java.util.HashMap<java.lang.String, com.proxy.service.widget.info.toast.ToastViewFactory.BaseViewInfo> toastMap = null;
    private static com.proxy.service.widget.info.toast.ToastConfig config;
    private static com.proxy.service.widget.info.toast.ToastViewFactory factory;
    private static android.widget.Toast currentToast;
    
    private CsToast() {
        super();
    }
    
    /**
     * 设置全局 toast 配置
     */
    public final void setGlobalToastConfig(@org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastConfig config) {
    }
    
    /**
     * 设置全局 toast 视图工厂
     */
    public final void setGlobalToastViewFactory(@org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastViewFactory factory) {
    }
    
    /**
     * 清空已缓存的 toast 对象
     */
    public final void clearToastCache() {
    }
    
    /**
     * 显示文字 toast, 单例
     */
    public final void show(@androidx.annotation.StringRes
    int stringId, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示文字 toast, 单例
     */
    public final void show(@org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示 icon与文字 toast, 单例
     */
    public final void show(@androidx.annotation.DrawableRes
    int resId, @androidx.annotation.StringRes
    int stringId, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示 icon与文字 toast, 单例
     */
    public final void show(@androidx.annotation.DrawableRes
    int resId, @org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示 icon与文字 toast, 单例
     */
    public final void show(@org.jetbrains.annotations.Nullable
    android.graphics.drawable.Drawable icon, @androidx.annotation.StringRes
    int stringId, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示 icon与文字 toast, 单例
     */
    public final void show(@org.jetbrains.annotations.Nullable
    android.graphics.drawable.Drawable icon, @org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示 icon与文字 toast, 单例
     */
    public final void show(@org.jetbrains.annotations.Nullable
    android.graphics.Bitmap icon, @androidx.annotation.StringRes
    int stringId, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * 显示 icon与文字 toast, 单例
     */
    public final void show(@org.jetbrains.annotations.Nullable
    android.graphics.Bitmap icon, @org.jetbrains.annotations.NotNull
    java.lang.String content, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastDuration duration, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
    }
    
    /**
     * * *** *** *** *** *** *** *** *** *** 私有函数 *** *** *** *** *** *** *** *** *** ***
     */
    private final android.widget.Toast createToast(android.content.Context context, android.view.View view) {
        return null;
    }
}