package com.proxy.service.widget.info.statepage.config;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/9 20:06
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0005H&J\b\u0010\u0007\u001a\u00020\u0005H&J2\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\u0010\b\u0002\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\rH&J>\u0010\u000e\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00012\u0010\b\u0002\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\rH&J\u0014\u0010\u0010\u001a\u00020\u00052\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0001H&J\b\u0010\u0011\u001a\u00020\u0005H&\u00a8\u0006\u0012"}, d2 = {"Lcom/proxy/service/widget/info/statepage/config/IStatePageController;", "", "getRootView", "Landroid/view/View;", "hideEmpty", "", "hideError", "hideLoading", "showEmpty", "message", "", "any", "buttonClick", "Lkotlin/Function0;", "showError", "buttonTxt", "showLoading", "showSuccess", "WidgetInfo_debug"})
public abstract interface IStatePageController {
    
    /**
     * 根 view
     */
    @org.jetbrains.annotations.NotNull
    public abstract android.view.View getRootView();
    
    /**
     * 加载成功, 显示正常页面, 自动隐藏全部状态页面(loading、error、empty)
     */
    public abstract void showSuccess();
    
    /**
     * 显示 loading
     *
     * @param any   自定义数据
     */
    public abstract void showLoading(@org.jetbrains.annotations.Nullable
    java.lang.Object any);
    
    /**
     * 隐藏 loading
     */
    public abstract void hideLoading();
    
    /**
     * 显示错误页面
     *
     * @param message       错误信息
     * @param buttonTxt     按钮文案
     * @param any           自定义数据
     * @param buttonClick   按钮点击
     */
    public abstract void showError(@org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.String buttonTxt, @org.jetbrains.annotations.Nullable
    java.lang.Object any, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> buttonClick);
    
    /**
     * 隐藏错误页面
     */
    public abstract void hideError();
    
    /**
     * 显示空数据页面
     *
     * @param message       空数据信息
     * @param any           自定义数据
     * @param buttonClick   按钮点击
     */
    public abstract void showEmpty(@org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.Object any, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> buttonClick);
    
    /**
     * 隐藏空数据页面
     */
    public abstract void hideEmpty();
    
    /**
     * @author: cangHX
     * @data: 2025/7/9 20:06
     * @desc:
     */
    @kotlin.Metadata(mv = {1, 7, 1}, k = 3)
    public final class DefaultImpls {
    }
}