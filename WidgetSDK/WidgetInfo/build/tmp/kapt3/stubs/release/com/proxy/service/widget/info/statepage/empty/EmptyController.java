package com.proxy.service.widget.info.statepage.empty;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/10 10:01
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J2\u0010\u0007\u001a\u00020\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00012\u0010\b\u0002\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\u0003\u0018\u00010\fH&\u00a8\u0006\r"}, d2 = {"Lcom/proxy/service/widget/info/statepage/empty/EmptyController;", "", "hide", "", "initView", "viewGroup", "Landroid/view/ViewGroup;", "show", "message", "", "any", "buttonClick", "Lkotlin/Function0;", "WidgetInfo_release"})
public abstract interface EmptyController {
    
    /**
     * 初始化
     */
    public abstract void initView(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup viewGroup);
    
    /**
     * 显示页面
     *
     * @param message       信息
     * @param any           自定义数据
     * @param buttonClick   按钮点击
     */
    public abstract void show(@org.jetbrains.annotations.Nullable
    java.lang.String message, @org.jetbrains.annotations.Nullable
    java.lang.Object any, @org.jetbrains.annotations.Nullable
    kotlin.jvm.functions.Function0<kotlin.Unit> buttonClick);
    
    /**
     * 隐藏页面
     */
    public abstract void hide();
    
    /**
     * @author: cangHX
     * @data: 2025/7/10 10:01
     * @desc:
     */
    @kotlin.Metadata(mv = {1, 7, 1}, k = 3)
    public final class DefaultImpls {
    }
}