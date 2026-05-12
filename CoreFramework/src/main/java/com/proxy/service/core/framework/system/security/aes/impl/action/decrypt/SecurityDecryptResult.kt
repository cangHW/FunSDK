package com.proxy.service.core.framework.system.security.aes.impl.action.decrypt

import com.proxy.service.core.framework.system.security.aes.base.action.IDecryptResult
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import java.nio.charset.StandardCharsets

/**
 * @author: cangHX
 * @date: 2024/12/3 11:40
 * @desc:
 */
open class SecurityDecryptResult(
    private val controller: IController
) : IDecryptResult {

    override fun getString(): String? {
        val result = controller.finish()
        if (controller.isError()) {
            return null
        }
        return String(result, StandardCharsets.UTF_8)
    }

    override fun getByteArray(): ByteArray? {
        val result = controller.finish()
        if (controller.isError()) {
            return null
        }
        return result
    }
}
