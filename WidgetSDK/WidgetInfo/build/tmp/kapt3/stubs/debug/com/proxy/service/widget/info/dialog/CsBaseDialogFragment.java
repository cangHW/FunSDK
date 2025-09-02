package com.proxy.service.widget.info.dialog;

import java.lang.System;

/**
 * 避免在 activity 不合适的生命周期添加 DialogFragment 导致 crash
 *
 * @author: cangHX
 * @data: 2025/7/8 11:13
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 \u00142\u00020\u0001:\u0001\u0014B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016J\u000e\u0010\f\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006J\u001a\u0010\f\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\bH\u0016J\u000e\u0010\f\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u001a\u0010\f\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\b\u0010\r\u001a\u0004\u0018\u00010\bH\u0016J\u000e\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006J\u0018\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\bJ\u000e\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006J\u001a\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\bH\u0016J\u000e\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006J\u0018\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\r\u001a\u0004\u0018\u00010\b\u00a8\u0006\u0015"}, d2 = {"Lcom/proxy/service/widget/info/dialog/CsBaseDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "()V", "checkActivityCanUse", "", "manager", "Landroidx/fragment/app/FragmentManager;", "funName", "", "dismiss", "", "dismissAllowingStateLoss", "show", "tag", "", "transaction", "Landroidx/fragment/app/FragmentTransaction;", "showAllowingStateLoss", "showNow", "showNowAllowingStateLoss", "Companion", "WidgetInfo_debug"})
public class CsBaseDialogFragment extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.dialog.CsBaseDialogFragment.Companion Companion = null;
    private static final java.lang.String TAG = "CoreFw_Widget_df";
    
    public CsBaseDialogFragment() {
        super();
    }
    
    public final void show(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager) {
    }
    
    @java.lang.Override
    public void show(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager, @org.jetbrains.annotations.Nullable
    java.lang.String tag) {
    }
    
    public final int show(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentTransaction transaction) {
        return 0;
    }
    
    @java.lang.Override
    public int show(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentTransaction transaction, @org.jetbrains.annotations.Nullable
    java.lang.String tag) {
        return 0;
    }
    
    public final void showNow(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager) {
    }
    
    @java.lang.Override
    public void showNow(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager, @org.jetbrains.annotations.Nullable
    java.lang.String tag) {
    }
    
    public final void showAllowingStateLoss(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager) {
    }
    
    public final void showAllowingStateLoss(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager, @org.jetbrains.annotations.Nullable
    java.lang.String tag) {
    }
    
    public final void showNowAllowingStateLoss(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager) {
    }
    
    public final void showNowAllowingStateLoss(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager, @org.jetbrains.annotations.Nullable
    java.lang.String tag) {
    }
    
    @java.lang.Override
    public void dismiss() {
    }
    
    @java.lang.Override
    public void dismissAllowingStateLoss() {
    }
    
    private final boolean checkActivityCanUse(androidx.fragment.app.FragmentManager manager, java.lang.String funName) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/proxy/service/widget/info/dialog/CsBaseDialogFragment$Companion;", "", "()V", "TAG", "", "WidgetInfo_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}