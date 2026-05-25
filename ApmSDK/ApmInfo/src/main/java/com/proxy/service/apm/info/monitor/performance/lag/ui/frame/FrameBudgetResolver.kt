package com.proxy.service.apm.info.monitor.performance.lag.ui.frame

import android.app.Activity
import com.proxy.service.apm.info.constants.Constants
import java.util.WeakHashMap

/**
 * 解析单帧有效渲染预算（deadline → 刷新率缓存 → 固定兜底）。
 */
object FrameBudgetResolver {

    private const val NS_PER_SECOND = 1_000_000_000L

    private val cache = WeakHashMap<Activity, FrameBudget>()

    fun resolve(deadlineNs: Long, activity: Activity): FrameBudget {
        if (deadlineNs > 0L) {
            return FrameBudget(deadlineNs, BudgetSource.DEADLINE_METRIC)
        }
        cache[activity]?.let { return it }
        val budget = resolveFromRefreshRate(activity)
        cache[activity] = budget
        return budget
    }

    fun evict(activity: Activity) {
        cache.remove(activity)
    }

    private fun resolveFromRefreshRate(activity: Activity): FrameBudget {
        val refreshRate = readRefreshRate(activity)
        if (refreshRate > 0f) {
            val budgetNs = (NS_PER_SECOND / refreshRate).toLong()
            if (budgetNs > 0L) {
                return FrameBudget(budgetNs, BudgetSource.REFRESH_RATE)
            }
        }
        return FrameBudget(
            Constants.MONITOR_UI_LAG_FALLBACK_BUDGET_NS,
            BudgetSource.FIXED_FALLBACK,
        )
    }

    private fun readRefreshRate(activity: Activity): Float {
        return try {
            activity.window?.decorView?.display?.refreshRate ?: 0f
        } catch (_: Throwable) {
            0f
        }
    }
}
