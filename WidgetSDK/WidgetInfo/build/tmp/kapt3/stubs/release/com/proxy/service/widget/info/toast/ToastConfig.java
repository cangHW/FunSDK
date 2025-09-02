package com.proxy.service.widget.info.toast;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/8 18:35
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0018\u001a\u00020\u0013X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0015\"\u0004\b\u001a\u0010\u0017\u00a8\u0006\u001b"}, d2 = {"Lcom/proxy/service/widget/info/toast/ToastConfig;", "", "()V", "gravity", "Lcom/proxy/service/widget/info/toast/ToastGravity;", "getGravity", "()Lcom/proxy/service/widget/info/toast/ToastGravity;", "setGravity", "(Lcom/proxy/service/widget/info/toast/ToastGravity;)V", "horizontalMargin", "", "getHorizontalMargin", "()F", "setHorizontalMargin", "(F)V", "verticalMargin", "getVerticalMargin", "setVerticalMargin", "xOffsetPx", "", "getXOffsetPx", "()I", "setXOffsetPx", "(I)V", "yOffsetPx", "getYOffsetPx", "setYOffsetPx", "WidgetInfo_release"})
public final class ToastConfig {
    
    /**
     * 距离屏幕左右间距百分比（0-1）
     */
    private float horizontalMargin = 0.0F;
    
    /**
     * 距离屏幕上下间距百分比（0-1）
     */
    private float verticalMargin = 0.1F;
    
    /**
     * 位于屏幕位置
     */
    @org.jetbrains.annotations.NotNull
    private com.proxy.service.widget.info.toast.ToastGravity gravity = com.proxy.service.widget.info.toast.ToastGravity.BOTTOM;
    
    /**
     * 距离屏幕原点偏移值
     */
    private int xOffsetPx = 0;
    
    /**
     * 距离屏幕原点偏移值
     */
    private int yOffsetPx = 0;
    
    public ToastConfig() {
        super();
    }
    
    public final float getHorizontalMargin() {
        return 0.0F;
    }
    
    public final void setHorizontalMargin(float p0) {
    }
    
    public final float getVerticalMargin() {
        return 0.0F;
    }
    
    public final void setVerticalMargin(float p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.proxy.service.widget.info.toast.ToastGravity getGravity() {
        return null;
    }
    
    public final void setGravity(@org.jetbrains.annotations.NotNull
    com.proxy.service.widget.info.toast.ToastGravity p0) {
    }
    
    public final int getXOffsetPx() {
        return 0;
    }
    
    public final void setXOffsetPx(int p0) {
    }
    
    public final int getYOffsetPx() {
        return 0;
    }
    
    public final void setYOffsetPx(int p0) {
    }
}