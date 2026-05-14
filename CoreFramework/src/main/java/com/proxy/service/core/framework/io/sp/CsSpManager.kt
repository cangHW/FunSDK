package com.proxy.service.core.framework.io.sp

import com.proxy.service.core.framework.io.sp.impl.SpActionImpl
import com.proxy.service.core.framework.io.sp.base.SpMode
import com.proxy.service.core.framework.io.sp.controller.ISpAction
import com.proxy.service.core.framework.io.sp.impl.SpConfigActionImpl

/**
 * 类sp，key-value，键值对存储相关工具
 *
 * @author: cangHX
 * @date: 2024/7/20 14:28
 * @desc:
 */
object CsSpManager : SpActionImpl(), ISpAction {

    override fun name(tag: String): ISpAction {
        val impl = SpConfigActionImpl()
        impl.name(tag)
        return impl
    }

    override fun mode(mode: SpMode): ISpAction {
        val impl = SpConfigActionImpl()
        impl.mode(mode)
        return impl
    }

    override fun secretKey(secretKey: String): ISpAction {
        val impl = SpConfigActionImpl()
        impl.secretKey(secretKey)
        return impl
    }
}