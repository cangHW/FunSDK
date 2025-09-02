package com.proxy.service.widget.info.statepage.loading;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/9 20:05
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H&J\u0012\u0010\u0007\u001a\u00020\u00032\b\u0010\b\u001a\u0004\u0018\u00010\u0001H&\u00a8\u0006\t"}, d2 = {"Lcom/proxy/service/widget/info/statepage/loading/LoadingController;", "", "hide", "", "initView", "viewGroup", "Landroid/view/ViewGroup;", "show", "any", "WidgetInfo_debug"})
public abstract interface LoadingController {
    
    /**
     * 初始化
     */
    public abstract void initView(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup viewGroup);
    
    /**
     * 显示
     *
     * @param any   自定义数据
     */
    public abstract void show(@org.jetbrains.annotations.Nullable
    java.lang.Object any);
    
    /**
     * 隐藏
     */
    public abstract void hide();
}