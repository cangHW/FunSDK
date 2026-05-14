package com.proxy.service.core.framework.io.sp.impl

import com.proxy.service.core.framework.io.sp.SpInit
import com.proxy.service.core.framework.io.sp.SpMode
import com.proxy.service.core.framework.io.sp.ISpAction
import com.proxy.service.core.framework.io.sp.ISpController

/**
 * @author: cangHX
 * @date: 2026/5/14 10:47
 * @desc:
 */
class SpConfigActionImpl : SpActionImpl(), ISpAction {

    private var name: String? = null
    private var mode = SpMode.SINGLE_PROCESS_MODE
    private var secretKey: String? = null

    override fun name(tag: String): ISpAction {
        this.name = tag
        return this
    }

    override fun mode(mode: SpMode): ISpAction {
        this.mode = mode
        return this
    }

    override fun secretKey(secretKey: String): ISpAction {
        this.secretKey = secretKey
        return this
    }

    override fun getSp(): ISpController {
        return SpInit.getSp(name, mode, secretKey)
    }
}