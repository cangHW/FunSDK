package com.proxy.service.core.framework.system.security.aes.impl.action.encrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IEncryptResult
import com.proxy.service.core.framework.system.security.aes.base.controller.IController

/**
 * @author: cangHX
 * @date: 2024/12/3 11:40
 * @desc:
 */
open class SecurityEncryptResult(
    private val controller: IController
) : IEncryptResult {

    override fun getString(): String? {
        val result = controller.finish()
        if (controller.isError()) {
            return null
        }
        return CsStringUtils.parseByte2HexStr(result)
    }

    override fun getBase64String(): String? {
        val result = controller.finish()
        if (controller.isError()) {
            return null
        }
        return CsStringUtils.parseByte2Base64Str(result)
    }

    override fun getByteArray(): ByteArray? {
        val result = controller.finish()
        if (controller.isError()) {
            return null
        }
        return result
    }
}
