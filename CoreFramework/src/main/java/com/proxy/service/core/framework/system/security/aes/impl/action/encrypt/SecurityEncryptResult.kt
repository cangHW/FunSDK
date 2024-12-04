package com.proxy.service.core.framework.system.security.aes.impl.action.encrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IEncryptResult
import com.proxy.service.core.framework.system.security.aes.base.controller.IController

/**
 * @author: cangHX
 * @data: 2024/12/3 11:40
 * @desc:
 */
open class SecurityEncryptResult(
    private val controller: IController
) : IEncryptResult {
    override fun getString(): String {
        return CsStringUtils.parseByte2HexStr(controller.finish()) ?: ""
    }

    override fun getBase64String(): String {
        return CsStringUtils.parseByte2Base64Str(controller.finish()) ?: ""
    }

    override fun getByteArray(): ByteArray {
        return controller.finish()
    }
}