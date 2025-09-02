package com.proxy.service.widget.info.view.recyclerview;

import java.lang.System;

/**
 * @author: cangHX
 * @data: 2025/7/9 15:32
 * @desc:
 */
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \u001a2\u00020\u0001:\u0002\u001a\u001bB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J(\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J0\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0013H\u0002J0\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0013H\u0002J(\u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002J(\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00192\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/proxy/service/widget/info/view/recyclerview/CsRecyclerItemDecoration;", "Landroidx/recyclerview/widget/RecyclerView$ItemDecoration;", "spaceInfo", "Lcom/proxy/service/widget/info/view/recyclerview/CsRecyclerItemDecoration$SpaceInfo;", "(Lcom/proxy/service/widget/info/view/recyclerview/CsRecyclerItemDecoration$SpaceInfo;)V", "getItemOffsets", "", "outRect", "Landroid/graphics/Rect;", "view", "Landroid/view/View;", "parent", "Landroidx/recyclerview/widget/RecyclerView;", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "gridLayoutManagerHorizontal", "layoutManager", "Landroidx/recyclerview/widget/GridLayoutManager;", "maxLine", "", "currentLine", "currentIndex", "gridLayoutManagerVertical", "processRectByGridLayoutManager", "processRectByLinearLayoutManager", "Landroidx/recyclerview/widget/LinearLayoutManager;", "Companion", "SpaceInfo", "WidgetInfo_release"})
public final class CsRecyclerItemDecoration extends androidx.recyclerview.widget.RecyclerView.ItemDecoration {
    private final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.SpaceInfo spaceInfo = null;
    @org.jetbrains.annotations.NotNull
    public static final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.Companion Companion = null;
    private static final java.lang.ThreadLocal<java.lang.Boolean> isSpaceShowOnViewTopLocal = null;
    private static final java.lang.ThreadLocal<java.lang.Boolean> isSpaceShowOnViewBottomLocal = null;
    private static final java.lang.ThreadLocal<java.lang.Boolean> isSpaceShowOnViewLeftLocal = null;
    private static final java.lang.ThreadLocal<java.lang.Boolean> isSpaceShowOnViewRightLocal = null;
    private static final java.lang.ThreadLocal<java.lang.Integer> firstLineOutSizeLocal = null;
    private static final java.lang.ThreadLocal<java.lang.Integer> lastLineOutSizeLocal = null;
    
    private CsRecyclerItemDecoration(com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.SpaceInfo spaceInfo) {
        super();
    }
    
    @java.lang.Override
    public void getItemOffsets(@org.jetbrains.annotations.NotNull
    android.graphics.Rect outRect, @org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView parent, @org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.State state) {
    }
    
    private final void processRectByLinearLayoutManager(androidx.recyclerview.widget.LinearLayoutManager layoutManager, android.graphics.Rect outRect, android.view.View view, androidx.recyclerview.widget.RecyclerView parent) {
    }
    
    private final void processRectByGridLayoutManager(androidx.recyclerview.widget.GridLayoutManager layoutManager, android.graphics.Rect outRect, android.view.View view, androidx.recyclerview.widget.RecyclerView parent) {
    }
    
    private final void gridLayoutManagerVertical(androidx.recyclerview.widget.GridLayoutManager layoutManager, android.graphics.Rect outRect, int maxLine, int currentLine, int currentIndex) {
    }
    
    private final void gridLayoutManagerHorizontal(androidx.recyclerview.widget.GridLayoutManager layoutManager, android.graphics.Rect outRect, int maxLine, int currentLine, int currentIndex) {
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0015\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\t\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u001a\u0010\u0011\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\f\"\u0004\b\u0012\u0010\u000eR\u001a\u0010\u0013\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\f\"\u0004\b\u0014\u0010\u000eR\u001e\u0010\u0015\u001a\u0004\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\t\u001a\u0004\b\u0016\u0010\u0006\"\u0004\b\u0017\u0010\bR\u001a\u0010\u0018\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001a\"\u0004\b\u001f\u0010\u001c\u00a8\u0006 "}, d2 = {"Lcom/proxy/service/widget/info/view/recyclerview/CsRecyclerItemDecoration$SpaceInfo;", "", "()V", "firstLineOutSize", "", "getFirstLineOutSize", "()Ljava/lang/Integer;", "setFirstLineOutSize", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "isSpaceShowOnViewBottom", "", "()Z", "setSpaceShowOnViewBottom", "(Z)V", "isSpaceShowOnViewLeft", "setSpaceShowOnViewLeft", "isSpaceShowOnViewRight", "setSpaceShowOnViewRight", "isSpaceShowOnViewTop", "setSpaceShowOnViewTop", "lastLineOutSize", "getLastLineOutSize", "setLastLineOutSize", "spaceHeightWithVertical", "getSpaceHeightWithVertical", "()I", "setSpaceHeightWithVertical", "(I)V", "spaceWidthWithHorizontal", "getSpaceWidthWithHorizontal", "setSpaceWidthWithHorizontal", "WidgetInfo_release"})
    static final class SpaceInfo {
        private int spaceHeightWithVertical = 0;
        private int spaceWidthWithHorizontal = 0;
        private boolean isSpaceShowOnViewTop = false;
        private boolean isSpaceShowOnViewBottom = false;
        private boolean isSpaceShowOnViewLeft = false;
        private boolean isSpaceShowOnViewRight = false;
        @org.jetbrains.annotations.Nullable
        private java.lang.Integer firstLineOutSize;
        @org.jetbrains.annotations.Nullable
        private java.lang.Integer lastLineOutSize;
        
        public SpaceInfo() {
            super();
        }
        
        public final int getSpaceHeightWithVertical() {
            return 0;
        }
        
        public final void setSpaceHeightWithVertical(int p0) {
        }
        
        public final int getSpaceWidthWithHorizontal() {
            return 0;
        }
        
        public final void setSpaceWidthWithHorizontal(int p0) {
        }
        
        public final boolean isSpaceShowOnViewTop() {
            return false;
        }
        
        public final void setSpaceShowOnViewTop(boolean p0) {
        }
        
        public final boolean isSpaceShowOnViewBottom() {
            return false;
        }
        
        public final void setSpaceShowOnViewBottom(boolean p0) {
        }
        
        public final boolean isSpaceShowOnViewLeft() {
            return false;
        }
        
        public final void setSpaceShowOnViewLeft(boolean p0) {
        }
        
        public final boolean isSpaceShowOnViewRight() {
            return false;
        }
        
        public final void setSpaceShowOnViewRight(boolean p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Integer getFirstLineOutSize() {
            return null;
        }
        
        public final void setFirstLineOutSize(@org.jetbrains.annotations.Nullable
        java.lang.Integer p0) {
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.Integer getLastLineOutSize() {
            return null;
        }
        
        public final void setLastLineOutSize(@org.jetbrains.annotations.Nullable
        java.lang.Integer p0) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\r\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fJ\u0016\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u0005J\u0016\u0010\u0014\u001a\u00020\u00002\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005J\u000e\u0010\u0017\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0007J\u000e\u0010\u0019\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0007J\u000e\u0010\u001a\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0007J\u000e\u0010\u001b\u001a\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/proxy/service/widget/info/view/recyclerview/CsRecyclerItemDecoration$Companion;", "", "()V", "firstLineOutSizeLocal", "Ljava/lang/ThreadLocal;", "", "isSpaceShowOnViewBottomLocal", "", "isSpaceShowOnViewLeftLocal", "isSpaceShowOnViewRightLocal", "isSpaceShowOnViewTopLocal", "lastLineOutSizeLocal", "createWithDp", "Lcom/proxy/service/widget/info/view/recyclerview/CsRecyclerItemDecoration;", "spaceVDp", "", "spaceHDp", "createWithPx", "spaceVPX", "spaceHPX", "setOutLineSpaceSize", "firstLineOutSize", "lastLineOutSize", "setShowOnBottom", "isShow", "setShowOnLeft", "setShowOnRight", "setShowOnTop", "WidgetInfo_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * 间距是否出现在顶部
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.Companion setShowOnTop(boolean isShow) {
            return null;
        }
        
        /**
         * 间距是否出现在底部
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.Companion setShowOnBottom(boolean isShow) {
            return null;
        }
        
        /**
         * 间距是否出现在左侧
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.Companion setShowOnLeft(boolean isShow) {
            return null;
        }
        
        /**
         * 间距是否出现在右侧
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.Companion setShowOnRight(boolean isShow) {
            return null;
        }
        
        /**
         * 设置行外间距默认大小
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration.Companion setOutLineSpaceSize(int firstLineOutSize, int lastLineOutSize) {
            return null;
        }
        
        /**
         * 创建一个间距管理类，并设置横向纵向间距，数值格式：dp
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration createWithDp(float spaceVDp, float spaceHDp) {
            return null;
        }
        
        /**
         * 创建一个间距管理类，并设置横向纵向间距，数值格式：px
         */
        @org.jetbrains.annotations.NotNull
        public final com.proxy.service.widget.info.view.recyclerview.CsRecyclerItemDecoration createWithPx(int spaceVPX, int spaceHPX) {
            return null;
        }
    }
}