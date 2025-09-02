package com.proxy.service.widget.info.toast;

import java.lang.System;

/**
 * toast 视图工厂
 *
 * @author: cangHX
 * @data: 2025/7/8 15:53
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0016\u0018\u00002\u00020\u0001:\u0003\u000b\f\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0017J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0017\u00a8\u0006\u000e"}, d2 = {"Lcom/proxy/service/widget/info/toast/ToastViewFactory;", "", "()V", "getToastView", "Lcom/proxy/service/widget/info/toast/ToastViewFactory$ToastViewInfo;", "context", "Landroid/content/Context;", "tag", "", "getToastViewWithIcon", "Lcom/proxy/service/widget/info/toast/ToastViewFactory$ToastWithIconViewInfo;", "BaseViewInfo", "ToastViewInfo", "ToastWithIconViewInfo", "WidgetInfo_debug"})
public class ToastViewFactory {
    
    public ToastViewFactory() {
        super();
    }
    
    /**
     * 获取普通 toast 视图
     */
    @org.jetbrains.annotations.NotNull
    @android.annotation.SuppressLint(value = {"MissingInflatedId"})
    public com.proxy.service.widget.info.toast.ToastViewFactory.ToastViewInfo getToastView(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
        return null;
    }
    
    /**
     * 获取带 icon 的 toast 视图
     */
    @org.jetbrains.annotations.NotNull
    @android.annotation.SuppressLint(value = {"MissingInflatedId"})
    public com.proxy.service.widget.info.toast.ToastViewFactory.ToastWithIconViewInfo getToastViewWithIcon(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    java.lang.String tag) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0016J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u0012\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\r\u001a\u00020\u000eH\u0016J\u0012\u0010\u000f\u001a\u00020\b2\b\b\u0001\u0010\r\u001a\u00020\u000eH\u0016J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0012"}, d2 = {"Lcom/proxy/service/widget/info/toast/ToastViewFactory$BaseViewInfo;", "", "rootView", "Landroid/view/View;", "(Landroid/view/View;)V", "getRootView", "()Landroid/view/View;", "updateIcon", "", "bitmap", "Landroid/graphics/Bitmap;", "drawable", "Landroid/graphics/drawable/Drawable;", "resId", "", "updateTxt", "content", "", "WidgetInfo_debug"})
    public static class BaseViewInfo {
        @org.jetbrains.annotations.NotNull
        private final android.view.View rootView = null;
        
        public BaseViewInfo(@org.jetbrains.annotations.NotNull
        android.view.View rootView) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.view.View getRootView() {
            return null;
        }
        
        public void updateIcon(@org.jetbrains.annotations.Nullable
        android.graphics.Bitmap bitmap) {
        }
        
        public void updateIcon(@org.jetbrains.annotations.Nullable
        android.graphics.drawable.Drawable drawable) {
        }
        
        public void updateIcon(@androidx.annotation.DrawableRes
        int resId) {
        }
        
        public void updateTxt(@org.jetbrains.annotations.NotNull
        java.lang.String content) {
        }
        
        public void updateTxt(@androidx.annotation.StringRes
        int resId) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/proxy/service/widget/info/toast/ToastViewFactory$ToastViewInfo;", "Lcom/proxy/service/widget/info/toast/ToastViewFactory$BaseViewInfo;", "rootView", "Landroid/view/View;", "textView", "Landroid/widget/TextView;", "(Landroid/view/View;Landroid/widget/TextView;)V", "updateTxt", "", "resId", "", "content", "", "WidgetInfo_debug"})
    public static class ToastViewInfo extends com.proxy.service.widget.info.toast.ToastViewFactory.BaseViewInfo {
        private final android.widget.TextView textView = null;
        
        public ToastViewInfo(@org.jetbrains.annotations.NotNull
        android.view.View rootView, @org.jetbrains.annotations.Nullable
        android.widget.TextView textView) {
            super(null);
        }
        
        @java.lang.Override
        public void updateTxt(@org.jetbrains.annotations.NotNull
        java.lang.String content) {
        }
        
        @java.lang.Override
        public void updateTxt(int resId) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0016\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bJ\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u0012\u0010\t\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\u0010\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/proxy/service/widget/info/toast/ToastViewFactory$ToastWithIconViewInfo;", "Lcom/proxy/service/widget/info/toast/ToastViewFactory$BaseViewInfo;", "rootView", "Landroid/view/View;", "iconView", "Landroid/widget/ImageView;", "textView", "Landroid/widget/TextView;", "(Landroid/view/View;Landroid/widget/ImageView;Landroid/widget/TextView;)V", "updateIcon", "", "bitmap", "Landroid/graphics/Bitmap;", "drawable", "Landroid/graphics/drawable/Drawable;", "resId", "", "updateTxt", "content", "", "WidgetInfo_debug"})
    public static class ToastWithIconViewInfo extends com.proxy.service.widget.info.toast.ToastViewFactory.BaseViewInfo {
        private final android.widget.ImageView iconView = null;
        private final android.widget.TextView textView = null;
        
        public ToastWithIconViewInfo(@org.jetbrains.annotations.NotNull
        android.view.View rootView, @org.jetbrains.annotations.Nullable
        android.widget.ImageView iconView, @org.jetbrains.annotations.Nullable
        android.widget.TextView textView) {
            super(null);
        }
        
        @java.lang.Override
        public void updateIcon(@org.jetbrains.annotations.Nullable
        android.graphics.Bitmap bitmap) {
        }
        
        @java.lang.Override
        public void updateIcon(@org.jetbrains.annotations.Nullable
        android.graphics.drawable.Drawable drawable) {
        }
        
        @java.lang.Override
        public void updateIcon(int resId) {
        }
        
        @java.lang.Override
        public void updateTxt(@org.jetbrains.annotations.NotNull
        java.lang.String content) {
        }
        
        @java.lang.Override
        public void updateTxt(int resId) {
        }
    }
}