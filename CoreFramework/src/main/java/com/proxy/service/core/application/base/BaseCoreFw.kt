package com.proxy.service.core.application.base

import android.app.Application
import com.proxy.service.base.BaseService

/**
 * 基础类, 用于优化性能
 *
 * @author: cangHX
 * @data: 2024/12/4 15:11
 * @desc:
 */
abstract class BaseCoreFw : BaseService {

    /**
     * 执行优先级，数字越小的优先执行[0 - ]
     * */
    open fun priority(): Int {
        return 0
    }

    abstract fun create(application: Application, isDebug: Boolean)

    /**
     * 初始化
     * */
    protected abstract fun onCreate(application: Application, isDebug: Boolean)
}