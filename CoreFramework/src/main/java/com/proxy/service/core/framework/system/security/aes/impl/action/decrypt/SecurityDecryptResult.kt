package com.proxy.service.core.framework.system.security.aes.impl.action.decrypt

import com.proxy.service.core.framework.system.security.aes.base.action.IDecryptResult
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import java.nio.charset.StandardCharsets

/**
 * @author: cangHX
 * @data: 2024/12/3 11:40
 * @desc:
 */
open class SecurityDecryptResult(
    private val controller: IController
) : IDecryptResult {
    override fun getString(): String {
        return String(controller.finish(), StandardCharsets.UTF_8)
    }

    override fun getByteArray(): ByteArray {
        return controller.finish()
    }
}