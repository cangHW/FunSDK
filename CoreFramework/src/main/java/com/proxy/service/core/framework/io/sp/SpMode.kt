package com.proxy.service.core.framework.io.sp

import com.tencent.mmkv.MMKV

/**
 * @author: cangHX
 * @data: 2024/7/20 14:38
 * @desc:
 */
enum class SpMode(val mode: Int) {
    /**
     * 单进程, 性能更高
     * */
    SINGLE_PROCESS_MODE(MMKV.SINGLE_PROCESS_MODE),

    /**
     * 多进程, 数据共享与同步
     * */
    MULTI_PROCESS_MODE(MMKV.MULTI_PROCESS_MODE);

}