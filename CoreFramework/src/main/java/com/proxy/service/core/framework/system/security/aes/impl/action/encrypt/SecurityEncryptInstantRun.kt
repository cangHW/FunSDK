package com.proxy.service.core.framework.system.security.aes.impl.action.encrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IInstantRun
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import java.nio.charset.StandardCharsets

/**
 * @author: cangHX
 * @data: 2024/12/4 10:09
 * @desc:
 */
class SecurityEncryptInstantRun(
    private val controller: IController
): IInstantRun {
    override fun updateSourceString(str: String): ByteArray {
        return controller.updateWithOutCache(str.toByteArray(StandardCharsets.UTF_8))
    }

    override fun updateSourceBase64String(base64: String): ByteArray {
        return CsStringUtils.parseBase64Str2Byte(base64)?.let {
            controller.updateWithOutCache(it)
        } ?: ByteArray(0)
    }

    override fun updateSourceByteArray(byteArray: ByteArray): ByteArray {
        return controller.updateWithOutCache(byteArray)
    }

    override fun endInstantRun(): ByteArray {
        return controller.finish()
    }
}