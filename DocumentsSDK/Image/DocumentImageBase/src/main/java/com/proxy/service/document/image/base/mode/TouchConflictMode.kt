package com.proxy.service.document.image.base.mode

/**
 * 图片预览触摸冲突处理模式.
 *
 * @author: cangHX
 * @date: 2026/5/6 10:10
 * @desc:
 */
enum class TouchConflictMode {
    /**
     * 默认行为, 图片预览始终消费触摸事件.
     * */
    AlwaysConsume,

    /**
     * 兼容 ViewPager2 / RecyclerView 等父容器, 根据图片边界动态释放父容器拦截.
     * */
    NestedScrollCompatible
}
