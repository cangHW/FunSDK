package com.proxy.service.widget.info.statepage.config;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/9 20:17
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\b"}, d2 = {"Lcom/proxy/service/widget/info/statepage/config/EmptyPageType;", "", "key", "", "(Ljava/lang/String;)V", "getKey", "()Ljava/lang/String;", "Companion", "WidgetInfo_debug"})
public final class EmptyPageType {
    @org.jetbrains.annotations.NotNull
    private final java.lang.String key = null;
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.statepage.config.EmptyPageType.Companion Companion = null;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WITH_OUT_REFRESH = "default_page_with_out_refresh";
    
    private EmptyPageType(java.lang.String key) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getKey() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0004J\u0006\u0010\t\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/proxy/service/widget/info/statepage/config/EmptyPageType$Companion;", "", "()V", "WITH_OUT_REFRESH", "", "buildDefaultPage", "Lcom/proxy/service/widget/info/statepage/config/EmptyPageType;", "buildPage", "key", "buildWithOutRefreshPage", "WidgetInfo_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.statepage.config.EmptyPageType buildDefaultPage() {
            return null;
        }
        
        /**
         * 不带刷新按钮效果
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.statepage.config.EmptyPageType buildWithOutRefreshPage() {
            return null;
        }
        
        /**
         * 自定义效果
         *
         * @param key 唯一标识
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.statepage.config.EmptyPageType buildPage(@org.jetbrains.annotations.NotNull
        java.lang.String key) {
            return null;
        }
    }
}