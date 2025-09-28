package com.proxy.service.core.application.base

import android.app.Application
import androidx.annotation.IntRange
import com.proxy.service.base.BaseService
import com.proxy.service.core.application.startup.PriorityUtils

/**
 * 基础类, 用于优化性能
 *
 * @author: cangHX
 * @data: 2024/12/4 15:11
 * @desc:
 */
abstract class BaseCoreFw : BaseService {

    enum class ModuleType {
        /**
         * 应用类型，初始化优先级[priority]最低为 [PriorityUtils.MIN_PRIORITY_NORMAL].
         * */
        APP,

        /**
         * SDK 类型，允许使用更大范围优先级配置, 初始化优先级[priority]最低为 [PriorityUtils.MIN_PRIORITY_WARNING].
         * */
        SDK;
    }

    /**
     * 模块类型, 用于控制是否开启更大优先级配置范围
     * */
    open fun moduleType(): ModuleType {
        return ModuleType.APP
    }

    /**
     * 执行优先级，数字越小的优先执行[0 - ]
     * */
    @IntRange(from = PriorityUtils.MIN_PRIORITY_NORMAL)
    open fun priority(): Int {
        return 0
    }

    abstract fun create(application: Application, isDebug: Boolean)

    /**
     * 初始化
     * */
    protected abstract fun onCreate(application: Application, isDebug: Boolean)
}