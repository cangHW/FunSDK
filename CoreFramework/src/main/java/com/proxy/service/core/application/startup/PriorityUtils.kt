package com.proxy.service.core.application.startup

import com.proxy.service.core.application.base.BaseCoreFw

/**
 * @author: cangHX
 * @data: 2025/9/28 14:25
 * @desc:
 */
internal object PriorityUtils {
    private const val PACKAGE_START = "com.proxy.service"
    const val MIN_PRIORITY_NORMAL: Long = 0
    const val MIN_PRIORITY_WARNING: Long = -500

    /**
     * 检查优先级配置
     * */
    fun checkPriority(baseCore: BaseCoreFw) {
        val priority = baseCore.priority()
        if (priority >= MIN_PRIORITY_NORMAL) {
            return
        }

        if (baseCore.moduleType() != BaseCoreFw.ModuleType.SDK) {
            throw PriorityRangeException("The app is not allowed to use a priority lower than 0. but ${baseCore.javaClass.name} priority=$priority")
        }

        if (priority >= MIN_PRIORITY_WARNING) {
            return
        }

        if (baseCore.javaClass.name.startsWith(PACKAGE_START)) {
            return
        }

        throw PriorityRangeException("The sdk does not allow the use of priorities lower than -500. but ${baseCore.javaClass.name} priority=$priority")
    }
}