package com.proxy.service.widget.info.statepage.loading.impl;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/10 10:33
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u0018\u0000 \u00162\u00020\u0001:\u0001\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\t\u001a\u0004\u0018\u00010\u00042\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002J\u0014\u0010\f\u001a\u0004\u0018\u00010\b2\b\u0010\n\u001a\u0004\u0018\u00010\rH\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0012\u0010\u0013\u001a\u00020\u000f2\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/proxy/service/widget/info/statepage/loading/impl/RotationLoading;", "Lcom/proxy/service/widget/info/statepage/loading/LoadingController;", "()V", "animator", "Landroid/animation/ObjectAnimator;", "binding", "Lcom/proxy/service/widget/info/databinding/CsWidgetStatePageLoadingRotationBinding;", "valueAnimator", "Landroid/animation/ValueAnimator;", "createAnim", "view", "Landroidx/appcompat/widget/AppCompatImageView;", "createBgAnim", "Landroid/view/View;", "hide", "", "initView", "viewGroup", "Landroid/view/ViewGroup;", "show", "any", "", "Companion", "WidgetInfo_release"})
public final class RotationLoading implements com.proxy.service.widget.info.statepage.loading.LoadingController {
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.statepage.loading.impl.RotationLoading.Companion Companion = null;
    private static final java.lang.String BG_START_COLOR = "#00000000";
    private static final java.lang.String BG_END_COLOR = "#33000000";
    private com.proxy.service.widget.info.databinding.CsWidgetStatePageLoadingRotationBinding binding;
    private android.animation.ObjectAnimator animator;
    private android.animation.ValueAnimator valueAnimator;
    
    public RotationLoading() {
        super();
    }
    
    @java.lang.Override
    public void initView(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup viewGroup) {
    }
    
    @java.lang.Override
    public void show(@org.jetbrains.annotations.Nullable
    java.lang.Object any) {
    }
    
    @java.lang.Override
    public void hide() {
    }
    
    private final android.animation.ObjectAnimator createAnim(androidx.appcompat.widget.AppCompatImageView view) {
        return null;
    }
    
    private final android.animation.ValueAnimator createBgAnim(android.view.View view) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/proxy/service/widget/info/statepage/loading/impl/RotationLoading$Companion;", "", "()V", "BG_END_COLOR", "", "BG_START_COLOR", "WidgetInfo_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}