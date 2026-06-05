package com.proxy.service.apm.info.constants

/**
 * @author: cangHX
 * @date: 2025/4/12 17:44
 * @desc:
 */
object Constants {

    const val TAG = "Apm_"

    const val DIVIDER = "*** *** *** *** *** *** *** *** *** *** *** *** *** ***"
    const val DIVIDER_HALF = "*** *** *** *** *** *** ***"

    const val TEMP_DIR_NAME = "temp"

    const val MONITOR_SAMPLER_SLIDING_WINDOW_SIZE = 50

    // 通用检测配置
    const val MONITOR_COMMON_FUN_ENABLE = true
    const val MONITOR_COMMON_CACHE_TIME = 7 * 24 * 60 * 60 * 1000L
    const val MONITOR_COMMON_MAX_ALL_FILE_SIZE_BYTE = 50 * 1024 * 1024L
    const val MONITOR_COMMON_MAX_FILE_COUNT = 5

    // 主线程慢分发检测配置
    const val MONITOR_PERF_MAIN_THREAD_LAG_WALL_TIME = 1000L

    // UI 渲染卡顿：无 deadline/刷新率时的固定帧预算（约 60fps，纳秒）
    const val MONITOR_UI_LAG_FALLBACK_BUDGET_NS = 16_666_666L
    const val MONITOR_UI_LAG_MILD_OVERRUN_MULTIPLIER = 1.0
    const val MONITOR_UI_LAG_MODERATE_OVERRUN_MULTIPLIER = 2.0
    const val MONITOR_UI_LAG_SEVERE_OVERRUN_MULTIPLIER = 4.0
    const val MONITOR_UI_LAG_MODERATE_PHASE_MULTIPLIER = 2.0
    const val MONITOR_UI_LAG_SEVERE_PHASE_MULTIPLIER = 4.0
    const val MONITOR_UI_LAG_REPORT_FIRST_DRAW_FRAME = false
    const val MONITOR_UI_LAG_NOTIFY_THROTTLE_MS = 3000L
    const val MONITOR_UI_LAG_ABSOLUTE_SEVERE_FLOOR_MS = 0L

    // 异常检测配置
    const val MONITOR_CRASH_LOGCAT_MAX_LINES = 500
    const val MONITOR_CRASH_LOGCAT_TIMEOUT_MS = 300L
    const val MONITOR_CRASH_LOGCAT_MAX_BYTES = 128 * 1024L
    const val MONITOR_CRASH_ALL_THREAD_STACK_MAX_THREAD = 40
    const val MONITOR_CRASH_ALL_THREAD_STACK_MAX_DEPTH_PER_THREAD = 64
    const val MONITOR_CRASH_ALL_THREAD_STACK_MAX_TOTAL_CHARS = 512 * 1024L

    // ANR 上报冷却：同一 ANR 风暴内多次 SIGQUIT 只上报一次
    const val MONITOR_ANR_REPORT_COOLDOWN_MS = 30_000L

    // Java 内存泄漏监测
    const val MONITOR_MEMORY_LEAK_RETAINED_CHECK_DELAY_MS = 5_000L
    const val MONITOR_MEMORY_LEAK_REFERENCE_QUEUE_POLL_INTERVAL_MS = 5_000L
    const val MONITOR_MEMORY_LEAK_RETAINED_VISIBLE_THRESHOLD = 3
    const val MONITOR_MEMORY_LEAK_RETAINED_BACKGROUND_THRESHOLD = 1
    const val MONITOR_MEMORY_LEAK_BACKGROUND_DUMP_DELAY_MS = 5_000L
    const val MONITOR_MEMORY_LEAK_NOTIFY_THROTTLE_MS = 3_000L
    const val MONITOR_MEMORY_LEAK_GC_REPEAT = 3
    const val MONITOR_MEMORY_LEAK_GC_INTERVAL_MS = 100L

}