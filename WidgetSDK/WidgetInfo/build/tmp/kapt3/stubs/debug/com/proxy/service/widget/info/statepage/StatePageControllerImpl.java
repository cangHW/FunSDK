package com.proxy.service.widget.info.statepage;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/9 20:21
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\b\u0010\u0013\u001a\u00020\u0003H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0015H\u0016J\b\u0010\u0017\u001a\u00020\u0015H\u0016J,\u0010\u0018\u001a\u00020\u00152\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u000e\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u001eH\u0016J6\u0010\u001f\u001a\u00020\u00152\b\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\b\u0010 \u001a\u0004\u0018\u00010\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u000e\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0015\u0018\u00010\u001eH\u0016J\u0012\u0010!\u001a\u00020\u00152\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0016J\b\u0010\"\u001a\u00020\u0015H\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/proxy/service/widget/info/statepage/StatePageControllerImpl;", "Lcom/proxy/service/widget/info/statepage/config/IStatePageController;", "view", "Landroid/view/View;", "loadingPageType", "Lcom/proxy/service/widget/info/statepage/config/LoadingPageType;", "emptyPageType", "Lcom/proxy/service/widget/info/statepage/config/EmptyPageType;", "errorPageType", "Lcom/proxy/service/widget/info/statepage/config/ErrorPageType;", "(Landroid/view/View;Lcom/proxy/service/widget/info/statepage/config/LoadingPageType;Lcom/proxy/service/widget/info/statepage/config/EmptyPageType;Lcom/proxy/service/widget/info/statepage/config/ErrorPageType;)V", "emptyController", "Lcom/proxy/service/widget/info/statepage/empty/EmptyController;", "errorController", "Lcom/proxy/service/widget/info/statepage/error/ErrorController;", "loadingController", "Lcom/proxy/service/widget/info/statepage/loading/LoadingController;", "rootView", "Landroid/widget/FrameLayout;", "getRootView", "hideEmpty", "", "hideError", "hideLoading", "showEmpty", "message", "", "any", "", "buttonClick", "Lkotlin/Function0;", "showError", "buttonTxt", "showLoading", "showSuccess", "WidgetInfo_debug"})
public final class StatePageControllerImpl implements com.proxy.service.widget.info.statepage.config.IStatePageController {
    private final android.widget.FrameLayout rootView = null;
    private com.proxy.service.widget.info.statepage.loading.LoadingController loadingController;
    private com.proxy.service.widget.info.statepage.empty.EmptyController emptyController;
    private com.proxy.service.widget.info.statepage.error.ErrorController errorController;
    
    public StatePageControllerImpl(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.LoadingPageType loadingPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.EmptyPageType emptyPageType, @org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.statepage.config.ErrorPageType errorPageType) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.view.View getRootView() {
        return null;
    }
    
    @java.lang.Override
    public void showSuccess() {
    }
    
    @java.lang.Override
    public void showLoading(@org.jetbrains.annotations.Nullable
    java.lang.Object any) {
    }
    
    @java.lang.Override
    public void hideLoading() {
    }
    
    @java.lang.Override
    public void showError(@org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.String buttonTxt, @org.jetbrains.annotations.Nullable
    java.lang.Object any, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> buttonClick) {
    }
    
    @java.lang.Override
    public void hideError() {
    }
    
    @java.lang.Override
    public void showEmpty(@org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.Object any, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> buttonClick) {
    }
    
    @java.lang.Override
    public void hideEmpty() {
    }
}