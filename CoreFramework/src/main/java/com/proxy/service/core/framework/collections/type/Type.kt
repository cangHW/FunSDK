package com.proxy.service.core.framework.collections.type

/**
 * @author: cangHX
 * @data: 2025/9/30 13:13
 * @desc:
 */
enum class Type {

    /**
     * 普通模式
     * */
    NORMAL,

    /**
     * 弱引用模式, 如果没有强引用指向 key, 则对应数据随时会被回收
     * */
    WEAK,

    /**
     * 顺序模式, 按插入顺序排序
     * */
    ORDER,

}